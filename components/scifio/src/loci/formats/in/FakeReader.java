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

package loci.formats.in;

import java.io.IOException;

import ome.scifio.Metadata;
import ome.scifio.fake.FakeFormat;

import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.SCIFIOFormatReader;
import loci.formats.meta.MetadataStore;
import loci.legacy.context.LegacyContext;

/**
 * FakeReader is the file format reader for faking input data.
 * It is mainly useful for testing.
 * <p>Examples:<ul>
 *  <li>showinf 'multi-series&amp;series=11&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake' -series 9</li>
 *  <li>showinf '8bit-signed&amp;pixelType=int8&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '8bit-unsigned&amp;pixelType=uint8&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '16bit-signed&amp;pixelType=int16&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '16bit-unsigned&amp;pixelType=uint16&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '32bit-signed&amp;pixelType=int32&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '32bit-unsigned&amp;pixelType=uint32&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '32bit-floating&amp;pixelType=float&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 *  <li>showinf '64bit-floating&amp;pixelType=double&amp;sizeZ=3&amp;sizeC=5&amp;sizeT=7&amp;sizeY=50.fake'</li>
 * </ul></p>
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/in/FakeReader.java">Trac</a>
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/in/FakeReader.java;hb=HEAD">Gitweb</a></dd></dl>
 */
@Deprecated
public class FakeReader extends SCIFIOFormatReader {

  // -- Constants --

  public static final int BOX_SIZE = ome.scifio.fake.FakeFormat.BOX_SIZE;

  public static final int DEFAULT_SIZE_X = ome.scifio.fake.FakeFormat.DEFAULT_SIZE_X;
  public static final int DEFAULT_SIZE_Y = ome.scifio.fake.FakeFormat.DEFAULT_SIZE_Y;
  public static final int DEFAULT_SIZE_Z = ome.scifio.fake.FakeFormat.DEFAULT_SIZE_Z;
  public static final int DEFAULT_SIZE_C = ome.scifio.fake.FakeFormat.DEFAULT_SIZE_C;
  public static final int DEFAULT_SIZE_T = ome.scifio.fake.FakeFormat.DEFAULT_SIZE_T;
  public static final int DEFAULT_PIXEL_TYPE = FormatTools.pixelTypeFromString(ome.scifio.fake.FakeFormat.DEFAULT_PIXEL_TYPE);
  public static final int DEFAULT_RGB_CHANNEL_COUNT = 
      ome.scifio.fake.FakeFormat.DEFAULT_RGB_CHANNEL_COUNT;
  public static final String DEFAULT_DIMENSION_ORDER = 
      ome.scifio.fake.FakeFormat.DEFAULT_DIMENSION_ORDER;

  // -- Fields --

  // -- Constructor --

  /** Constructs a new fake reader. */
  public FakeReader() { 
    super("Simulated data", "fake");
  
    try {
      format = new FakeFormat(LegacyContext.get());
      checker = format.createChecker();
      parser = format.createParser();
      reader = format.createReader();
    }
    catch (ome.scifio.FormatException e) {
      LOGGER.warn("Failed to create APNGFormat components");
    }
  }

  // -- IFormatReader API methods --

  /* @see IFormatReader#get8BitLookupTable() */
  @Override
  public byte[][] get8BitLookupTable() throws FormatException, IOException {
    try {
      return reader.getDatasetMetadata().get8BitLookupTable(getSeries());
    } catch (ome.scifio.FormatException e) {
      throw new FormatException(e.getCause());
    }
  }

  /* @see IFormatReader#get16BitLookupTable() */
  @Override
  public short[][] get16BitLookupTable() throws FormatException, IOException {
    try {
      return reader.getDatasetMetadata().get16BitLookupTable(getSeries());
    } catch (ome.scifio.FormatException e) {
      throw new FormatException(e.getCause());
    }
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    try {
      return reader.openPlane(getSeries(), no, buf, x, y, w, h);
    } catch (ome.scifio.FormatException e) {
      throw new FormatException(e.getCause());
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    
    Metadata meta = null;
    
    try {
      meta = parser.parse(id);
    } catch (ome.scifio.FormatException e) {
      throw new FormatException(e.getCause());
    }
    
    reader.setMetadata(meta);
    
    // reinitialize the MetadataStore
    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
    
    for (int s=0; s<getSeriesCount(); s++) {
      String imageName = id.substring(0, id.lastIndexOf(".")) + (s > 0 ? (s + 1) : "");
      store.setImageName(imageName, s);
    }
  }
}
