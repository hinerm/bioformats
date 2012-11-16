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

import net.imglib2.display.ColorTable;

/**
 * Abstract superclass for {@link Plane} implementations in SCIFIO.
 * 
 * @author Mark Hiner
 *
 */
public abstract class AbstractPlane<T> extends AbstractHasContext 
  implements DataPlane<T> {

  // -- Fields --
  
  /** Native pixel data for this plane. */
  private T data = null;
  
  /** Color look-up table for this plane. */
  private ColorTable lut = null;
  
  // -- Constructor --
  
  public AbstractPlane(SCIFIO ctx) {
    super(ctx);
  }
  
  // -- Plane API methods --

  /* @see Plane#setColorTable(ColorTable) */
  public void setColorTable(ColorTable lut) {
    this.lut = lut;
  }
  
  /* @see Plane#getColorTable() */
  public ColorTable getColorTable() {
    return lut;
  }
  
  // -- DataPlane API methods --
  
  /* @see DataPlane#setData() */
  public void setData(T data) {
    this.data = data;
  }
  
  /* @see DataPlane#getData() */
  public T getData() {
    return data;
  }
}
