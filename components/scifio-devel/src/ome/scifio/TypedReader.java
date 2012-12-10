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

import java.io.IOException;

import ome.scifio.io.RandomAccessInputStream;

/**
 * TODO
 * @author Mark Hiner
 *
 * @param <M> - {@link ome.scifio.Metadata} used by this reader for reading images.
 * @param <P> - {@link ome.scifio.Plane} type returned by this reader.
 * @param <T> - data type for {@code Planes} returned by this reader. 
 */
public interface TypedReader<M extends TypedMetadata, P extends DataPlane<?>> extends Reader {


  /**
   * Obtains the specified image plane from the current file as a byte array.
   * @see #openPlane(int, int, P)
   */
  P openPlane(int imageIndex, int planeIndex)
    throws FormatException, IOException;

  /**
   * Obtains a sub-image of the specified image plane,
   * whose upper-left corner is given by (x, y).
   */
  P openPlane(int imageIndex, int planeIndex, int x, int y, int w, int h)
    throws FormatException, IOException;

  /**
   * Obtains the specified image plane from the current file into a
   * pre-allocated byte array of
   * (sizeX * sizeY * bytesPerPixel * RGB channel count).
   *
   * @param imageIndex the image index within the file.
   * @param planeIndex the plane index within the image.
   * @param plane a pre-allocated Plane
   * @return the pre-allocated Plane <code>plane</code> for convenience.
   * @throws FormatException if there was a problem parsing the metadata of the
   *   file.
   * @throws IOException if there was a problem reading the file.
   */
  P openPlane(int imageIndex, int planeIndex, P plane)
    throws FormatException, IOException;

  /**
   * Obtains a sub-image of the specified image plane
   * into a pre-allocated byte array.
   *
   * @param imageIndex the image index within the file.
   * @param planeIndex the plane index within the image.
   * @param plane a pre-allocated Plane
   * @param dims a map of dimension labels (e.g., "x", "y") to the size of the
   *             corresponding dimension (e.g., sizeX, sizeY) 
   * @return the pre-allocated Plane <code>plane</code> for convenience.
   * @throws FormatException if there was a problem parsing the metadata of the
   *   file.
   * @throws IOException if there was a problem reading the file.
   */
  P openPlane(int imageIndex, int planeIndex, P plane, int x, int y,
    int w, int h) throws FormatException, IOException;

  /**
   * Obtains a thumbnail for the specified image plane from the current file.
   */
  P openThumbPlane(int imageIndex, int planeIndex)
    throws FormatException, IOException;
  
  /** Reads a raw plane from disk. */
  P readPlane(RandomAccessInputStream s, int imageIndex, int x, int y,
    int w, int h, P plane) throws IOException;

  /** Reads a raw plane from disk. */
  P readPlane(RandomAccessInputStream s, int imageIndex, int x, int y,
    int w, int h, int scanlinePad, P plane) throws IOException;
  
  /** Sets the Metadata for this Reader */
  void setMetadata(M meta) throws IOException;

  /** Gets the type-specific Metadata for this Reader */
  M getMetadata();
  
  /** 
   * Creates a blank plane compatible with this reader.
   * @param xOffset
   * @param yOffset
   * @param xLength
   * @param yLength
   * @return
   */
  P createPlane(int xOffset, int yOffset, int xLength, int yLength);
}
