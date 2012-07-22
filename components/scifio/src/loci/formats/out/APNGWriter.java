//
// APNGWriter.java
//

/*
OME Bio-Formats package for reading and converting biological file formats.
Copyright (C) 2005-@year@ UW-Madison LOCI and Glencoe Software, Inc.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package loci.formats.out;

import java.io.IOException;

import ome.scifio.apng.APNGFormat;

import loci.formats.FormatException;
import loci.formats.SCIFIOFormatWriter;

/**
 * APNGWriter is the file format writer for PNG and APNG files.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/out/APNGWriter.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/out/APNGWriter.java;hb=HEAD">Gitweb</a></dd></dl>
 */
public class APNGWriter extends SCIFIOFormatWriter<APNGFormat.Metadata> {

  // -- Constants --

  // -- Fields --

  // -- Constructor --

  public APNGWriter() {
    super("Animated PNG", "png");
    writer = new ome.scifio.apng.APNGFormat.Writer();
  }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Deprecated
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    try {
      writer.saveBytes(getSeries(), no, buf, x, y, w, h);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }

  /* @see loci.formats.IFormatWriter#canDoStacks() */
  @Deprecated
  public boolean canDoStacks() {
    return writer.canDoStacks();
  }

  /* @see loci.formats.IFormatWriter#getPixelTypes(String) */
  @Deprecated
  public int[] getPixelTypes(String codec) {
    return writer.getPixelTypes(codec);
  }

  // -- IFormatHandler API methods --

  /* @see loci.formats.IFormatHandler#setId(String) */
  @Deprecated
  public void setId(String id) throws FormatException, IOException {
    try {
      writer.setDest(id);
    }
    catch (ome.scifio.FormatException e) {
      throw new FormatException(e);
    }
  }
}
