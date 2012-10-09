/*
 * #%L
 * OME SCIFIO package for reading and converting scientific file formats.
 * %%
 * Copyright (C) 2005 - 2012 Open Microscopy Environment:
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 *   - University of Dundee
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package ome.scifio;

import java.io.File;
import java.io.IOException;

import net.imglib2.meta.Axes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ome.scifio.FormatException;
import ome.scifio.io.RandomAccessInputStream;
import ome.scifio.util.FormatTools;

/**
 * Abstract superclass of all SCIFIO reader components.
 *
 */
public abstract class AbstractReader<M extends Metadata>
  extends AbstractHasContext implements Reader<M> {

  // -- Constants --

  protected static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

  /** Default thumbnail width and height. */
  protected static final int THUMBNAIL_DIMENSION = 128;

  // -- Fields --

  /** Type-specific Metadata values. */
  protected M metadata;

  /** Core Metadata values. */
  protected CoreMetadata cMeta;

  /** Whether or not to group multi-file formats. */
  protected boolean group = true;

  /** Whether or not to normalize float data. */
  protected boolean normalizeData;

  /** Current file. */
  protected RandomAccessInputStream in;

  /** List of domains in which this format is used. */
  protected String[] domains = new String[0];

  /** Name of current file. */
  protected String currentId;

  /** Whether this format supports multi-file datasets. */
  protected boolean hasCompanionFiles = false;

  // -- Constructors --

  /** Constructs a reader with the given context */
  public AbstractReader(final SCIFIO ctx)
  {
    super(ctx);
    init();
  }

  // -- HasFormat API Methods --

  @SuppressWarnings("unchecked")
  public Format<M, ?, ?, ?, ?> getFormat() {
    return getContext().getFormatFromReader(getClass());
  }

  // -- Reader API Methods --

  //TODO Merge common Reader and Writer API methods

  /* @see Reader#setSource(File) */
  public void setSource(final File file) throws IOException {
    setSource(file.getName());
  }

  /* @see Reader#setSource(String) */
  public void setSource(final String fileName) throws IOException {
    setSource(new RandomAccessInputStream(fileName));
  }

  /* @see Reader#setSource(RandomAccessInputStream) */
  public void setSource(final RandomAccessInputStream stream)
    throws IOException
  {
    in = stream;
    currentId = stream.getFileName();

    if (metadata == null) {
      try {
        @SuppressWarnings("unchecked")
        final M meta =
          (M) getContext().getFormatFromReader(getClass())
            .createParser()
            .parse(stream);
        setMetadata(meta);
      }
      catch (final FormatException e) {
        throw new IOException(e);
      }
    }
  }

  /* @see Reader#openBytes(int, int) */
  public byte[] openBytes(final int imageIndex, final int planeNumber)
    throws FormatException, IOException
  {
    return openBytes(
      imageIndex, planeNumber, 0, 0, cMeta.getAxisLength(imageIndex, Axes.X),
      cMeta.getAxisLength(imageIndex, Axes.Y));
  }

  /* @see Reader#openBytes(int, int, int, int, int, int) */
  public byte[] openBytes(final int imageIndex, final int planeIndex,
    final int x, final int y, final int w, final int h)
    throws FormatException, IOException
  {
    final int bpp =
      FormatTools.getBytesPerPixel(cMeta.getPixelType(imageIndex));
    final int ch = cMeta.getRGBChannelCount(imageIndex);
    final byte[] newBuffer = new byte[w * h * ch * bpp];
    return openBytes(imageIndex, planeIndex, newBuffer, x, y, w, h);
  }

  /* @see Reader#openBytes(int, int, byte[]) */
  public byte[] openBytes(final int imageIndex, final int planeIndex,
    final byte[] buf) throws FormatException, IOException
  {
    return openBytes(
      imageIndex, planeIndex, buf, 0, 0,
      cMeta.getAxisLength(imageIndex, Axes.X),
      cMeta.getAxisLength(imageIndex, Axes.Y));
  }

  /* @see Reader#openBytes(int, int, byte[], int, int, int, int) */
  public abstract byte[] openBytes(int imageIndex, int planeIndex, byte[] buf,
    int x, int y, int w, int h) throws FormatException, IOException;

  /* @see Reader#openPlane(int, int, int, int, int, int int) */
  public Object openPlane(final int imageIndex, final int planeIndex,
    final int x, final int y, final int w, final int h)
    throws FormatException, IOException
  {
    // NB: Readers use byte arrays by default as the native type.
    return openBytes(imageIndex, planeIndex, x, y, w, h);
  }

  /* @see Reader#readPlane(RandomAccessInputStream, int, int, int, int, int, int, byte[] */
  public byte[] readPlane(final RandomAccessInputStream s,
    final int imageIndex, final int x, final int y, final int w, final int h,
    final int scanlinePad, final byte[] buf) throws IOException
  {
    final int c = cMeta.getRGBChannelCount(imageIndex);
    final int bpp =
      FormatTools.getBytesPerPixel(cMeta.getPixelType(imageIndex));
    if (x == 0 && y == 0 && w == cMeta.getAxisLength(imageIndex, Axes.X) &&
      h == cMeta.getAxisLength(imageIndex, Axes.Y) && scanlinePad == 0)
    {
      s.read(buf);
    }
    else if (x == 0 && w == cMeta.getAxisLength(imageIndex, Axes.Y) &&
      scanlinePad == 0)
    {
      if (cMeta.isInterleaved(imageIndex)) {
        s.skipBytes(y * w * bpp * c);
        s.read(buf, 0, h * w * bpp * c);
      }
      else {
        final int rowLen = w * bpp;
        for (int channel = 0; channel < c; channel++) {
          s.skipBytes(y * rowLen);
          s.read(buf, channel * h * rowLen, h * rowLen);
          if (channel < c - 1) {
            // no need to skip bytes after reading final channel
            s.skipBytes((cMeta.getAxisLength(imageIndex, Axes.Y) - y - h) *
              rowLen);
          }
        }
      }
    }
    else {
      final int scanlineWidth =
        cMeta.getAxisLength(imageIndex, Axes.Y) + scanlinePad;
      if (cMeta.isInterleaved(imageIndex)) {
        s.skipBytes(y * scanlineWidth * bpp * c);
        for (int row = 0; row < h; row++) {
          s.skipBytes(x * bpp * c);
          s.read(buf, row * w * bpp * c, w * bpp * c);
          if (row < h - 1) {
            // no need to skip bytes after reading final row
            s.skipBytes(bpp * c * (scanlineWidth - w - x));
          }
        }
      }
      else {
        for (int channel = 0; channel < c; channel++) {
          s.skipBytes(y * scanlineWidth * bpp);
          for (int row = 0; row < h; row++) {
            s.skipBytes(x * bpp);
            s.read(buf, channel * w * h * bpp + row * w * bpp, w * bpp);
            if (row < h - 1 || channel < c - 1) {
              // no need to skip bytes after reading final row of final channel
              s.skipBytes(bpp * (scanlineWidth - w - x));
            }
          }
          if (channel < c - 1) {
            // no need to skip bytes after reading final channel
            s.skipBytes(scanlineWidth * bpp *
              (cMeta.getAxisLength(imageIndex, Axes.Y) - y - h));
          }
        }
      }
    }
    return buf;
  }

  /* @see Reader#openThumbBytes(int) */
  public byte[] openThumbBytes(final int imageIndex, final int planeIndex)
    throws FormatException, IOException
  {
    FormatTools.assertStream(in, true, 1);
    /* TODO move FormatTools implementation here 
    return FormatTools.openThumbBytes(this, no); */
    return null;
  }

  /* @see Reader#setGroupFiles(boolean) */
  public void setGroupFiles(final boolean groupFiles) {
    group = groupFiles;
  }

  /* @see Reader#isGroupFiles() */
  public boolean isGroupFiles() {
    FormatTools.assertStream(in, false, 1);
    return group;
  }

  /* @see Reader#fileGroupOption(String) */
  public int fileGroupOption(final String id)
    throws FormatException, IOException
  {
    return FormatTools.CANNOT_GROUP;
  }

  /* @see Reader#setMetadata() */
  public void setMetadata(final M meta) throws IOException {
    metadata = meta;
    cMeta = new CoreMetadata();
    if(in == null) setSource(meta.getSource());
    
    try {
      getFormat().findSourceTranslator(CoreMetadata.class).translate(meta, cMeta);
    } catch (FormatException e) {
      LOGGER.debug(e.getMessage());
    }
  }

  /* @see Reader#getMetadata() */
  public M getMetadata() {
    return metadata;
  }

  /* @see Reader#getCurrentFile() */
  public String getCurrentFile() {

    FormatTools.assertStream(in, true, 1);
    return in.getFileName();
  }

  /* @see Reader#close(boolean) */
  public void close(final boolean fileOnly) throws IOException {
    if (in != null) in.close();
    if (!fileOnly) {
      in = null;
    }
  }

  /* @see Reader#hasCompanionFiles() */
  public boolean hasCompanionFiles() {
    return hasCompanionFiles;
  }

  /* @see Reader#close() */
  public void close() throws IOException {
    close(false);
  }

  /* @see Reader#isNormalized() */
  public boolean isNormalized() {
    return normalizeData;
  }

  /* @see Reader#setNormalized(boolean) */
  public void setNormalized(final boolean normalize) {
    normalizeData = normalize;
  }

  /* @see Reader#getOptimalTileWidth(int) */
  public int getOptimalTileWidth(final int imageIndex) {
    return cMeta.getAxisLength(imageIndex, Axes.Y);
  }

  /* @see Reader#getOptimalTileHeight(int) */
  public int getOptimalTileHeight(final int imageIndex) {
    final int bpp =
      FormatTools.getBytesPerPixel(cMeta.getPixelType(imageIndex));
    final int maxHeight =
      (1024 * 1024) /
        (cMeta.getAxisLength(imageIndex, Axes.X) *
          cMeta.getRGBChannelCount(imageIndex) * bpp);
    return Math.min(maxHeight, cMeta.getAxisLength(imageIndex, Axes.X));
  }

  /* @see Reader#getDomains() */
  public String[] getDomains() {
    return domains;
  }

  /* @see Reader#getCoreMetadata() */
  public CoreMetadata getCoreMetadata() {
    return cMeta;
  }

  /* @see Reader#getStream() */
  public RandomAccessInputStream getStream() {
    return in;
  }

  /* @see Reader#getImageCount() */
  public int getImageCount() {
    return cMeta.getImageCount();
  }

  /* @see Reader#getPlaneCount(int) */
  public int getPlaneCount(final int imageIndex) {
    return cMeta.getPlaneCount(imageIndex);
  }

  public Reader<Metadata>[] getUnderlyingReaders() {
    // TODO Auto-generated method stub
    return null;
  }

  // -- AbstractReader Methods --

  private void init() {
    cMeta = new CoreMetadata();
  }

  public byte[] readPlane(final RandomAccessInputStream s,
    final int imageIndex, final int x, final int y, final int w, final int h,
    final byte[] buf) throws IOException
  {
    return readPlane(s, imageIndex, x, y, w, h, 0, buf);
  }
}
