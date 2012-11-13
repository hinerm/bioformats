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

import java.util.Hashtable;

import net.imglib2.meta.AxisType;

/**
 * TODO
 * 
 * @author Mark Hiner
 *
 */
public interface ImageMetadata {

  void setThumbSizeX(int thumbSizeX);

  void setThumbSizeY(int thumbSizeY);

  void setPixelType(int pixelType);

  void setBitsPerPixel(int bitsPerPixel);

  void setChannelLengths(int[] cLengths);

  void setChannelTypes(String[] cTypes);

  void setOrderCertain(boolean orderCertain);

  void setRgb(boolean rgb);

  void setLittleEndian(boolean littleEndian);

  void setInterleaved(boolean interleaved);

  void setIndexed(boolean indexed);

  void setFalseColor(boolean falseColor);

  void setMetadataComplete(boolean metadataComplete);

  void setImageMetadata(Hashtable<String, Object> imageMetadata);

  void setThumbnail(boolean thumbnail);

  void setAxisTypes(AxisType[] axisTypes);

  void setAxisLengths(int[] axisLengths);

  void setAxisLength(AxisType axis, int length);

  void setAxisType(int index, AxisType axis);

  void setPlaneCount(int planeCount);

  int getPlaneCount();

  int getThumbSizeX();

  int getThumbSizeY();

  int getPixelType();

  int getBitsPerPixel();

  int[] getChannelLengths();

  String[] getChannelTypes();

  AxisType[] getAxisTypes();

  int[] getAxisLengths();

  boolean isOrderCertain();

  boolean isRgb();

  boolean isLittleEndian();

  boolean isInterleaved();

  boolean isIndexed();

  boolean isFalseColor();

  boolean isMetadataComplete();

  Hashtable<String, Object> getImageMetadata();

  boolean isThumbnail();

}