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

package ome.scifio.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.imglib2.display.ColorTable16;
import net.imglib2.display.ColorTable8;
import ome.scifio.AbstractReader;
import ome.scifio.BufferedImagePlane;
import ome.scifio.FormatException;
import ome.scifio.Metadata;
import ome.scifio.SCIFIO;
import ome.scifio.common.DataTools;
import ome.scifio.util.FormatTools;

/**
 * BufferedImageReader is the superclass for file format readers
 * that use java.awt.image.BufferedImage as the native data type.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/in/BIFormatReader.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/in/BIFormatReader.java;hb=HEAD">Gitweb</a></dd></dl>
 *
 * @author Curtis Rueden ctrueden at wisc.edu
 */
public abstract class BufferedImageReader<M extends Metadata>
  extends AbstractReader<M, BufferedImagePlane> {
  // -- Constructors --

  /** Constructs a new BIFormatReader. */
  public BufferedImageReader(final SCIFIO ctx) {
    super(ctx);
  }

  // -- Reader API methods --

  /**
   * @see ome.scifio.Reader#openPlane(int, byte[], int, int, int, int)
   */
  @Override
  public BufferedImagePlane openPlane(final int imageIndex, 
    final int planeIndex, final BufferedImagePlane plane, final int x,
    final int y, final int w, final int h) throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(
      this, imageIndex, planeIndex, plane.getSize(), x, y, w, h);

    final BufferedImagePlane tempPlane =
      openPlane(imageIndex, planeIndex, x, y, w, h);
    if(tempPlane.getColorTable() instanceof ColorTable8) {
        final byte[] t = AWTImageTools.getBytes(tempPlane.getData(), false);
        System.arraycopy(t, 0, plane, 0, Math.min(t.length, plane.getSize()));
        break;
    }
    else if(tempPlane.getColorTable() instanceof ColorTable16) {
        final short[][] ts = AWTImageTools.getShorts(tempPlane.getData());
        for (int c = 0; c < ts.length; c++) {
          int offset = c * ts[c].length * 2;
          for (int i = 0; i < ts[c].length && offset < plane.length; i++) {
            DataTools.unpackBytes(
              ts[c][i], plane.getBytes(), offset, 2, dMeta.isLittleEndian(planeIndex));
            offset += 2;
          }
        }
        break;
    }
    return plane;
  }
  
  public BufferedImagePlane createPlane() {
    return new BufferedImagePlane(getContext());
  }

  // -- BufferedImageReader methods --

  public Class<?> getNativeDataType() {
    return BufferedImage.class;
  }

}
