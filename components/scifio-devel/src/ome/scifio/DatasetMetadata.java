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

import java.util.Collection;
import java.util.Hashtable;

import net.imglib2.meta.AxisType;

/**
 * TODO
 * 
 * @author Mark Hiner
 *
 */
public interface DatasetMetadata<M extends ImageMetadata> extends Metadata {

  Object getMetadataValue(int imageIndex, String field);

  Object getImageMetadataValue(int imageIndex, String field);

  Hashtable<String, Object> getDatasetMetadata();

  Hashtable<String, Object> getImageMetadata(int imageIndex);

  int getImageCount();

  int getPlaneCount(int imageIndex);

  boolean isInterleaved(int imageIndex);

  int getPixelType(int imageIndex);

  int getEffectiveSizeC(int imageIndex);

  int getRGBChannelCount(int imageIndex);

  boolean isLittleEndian(int imageIndex);

  boolean isIndexed(int imageIndex);

  int getBitsPerPixel(int imageIndex);

  boolean isRGB(int imageIndex);

  boolean isFalseColor(int imageIndex);

  int[] getChannelDimLengths(int imageIndex);

  String[] getChannelDimTypes(int imageIndex);

  int getThumbSizeX(int imageIndex);

  int getThumbSizeY(int imageIndex);

  /**
   * Returns the number of axes (planes) in the
   * specified image.
   * 
   * @param imageIndex - index for multi-image files
   * @return The axis/plane count
   */
  int getAxisCount(int imageIndex);

  /**
   * Gets the type of the (zero-indexed) specified plane.
   * 
   * @param imageIndex - index for multi-image files
   * @param planeIndex - index of the desired plane within the specified image
   * @return Type of the desired plane.
   */
  AxisType getAxisType(int imageIndex, int planeIndex);

  /**
   * Gets the length of the (zero-indexed) specified plane.
   * 
   * @param imageIndex - index for multi-image files
   * @param planeIndex - index of the desired plane within the specified image
   * @return Length of the desired plane.
   */
  int getAxisLength(int imageIndex, int planeIndex);

  /**
   * A convenience method for looking up the length of an axis
   * based on its type. No knowledge of plane ordering is necessary.
   * 
   * @param imageIndex - index for multi-image files
   * @param t - desired axis type
   * @return
   */
  int getAxisLength(int imageIndex, AxisType t);

  /**
   * Returns the array index for the specified AxisType. This index
   * can be used in other Axes methods for looking up lengths, etc...
   * </br></br>
   * This method can also be used as an existence check for the
   * targe AxisType.
   * 
   * @param imageIndex - index for multi-image files
   * @param type - axis type to look up
   * @return The index of the desired axis or -1 if not found.
   */
  int getAxisIndex(int imageIndex, AxisType type);

  /**
   * Returns an array of the types for axes associated with
   * the specified image index. Order is consistent with the
   * axis length (int) array returned by 
   * {@link DatasetMetadata#getAxesLengths(int)}.
   * </br></br>
   * AxisType order is sorted and represents order within the image.
   * 
   * @param imageIndex - index for multi-image sources
   * @return An array of AxisTypes in the order they appear.
   */
  AxisType[] getAxes(int imageIndex);

  /**
   * Returns an array of the lengths for axes associated with
   * the specified image index.
   * 
   * Ordering is consistent with the 
   * AxisType array returned by {@link DatasetMetadata#getAxes(int)}.
   * 
   * @param imageIndex
   * @return
   */
  int[] getAxesLengths(int imageIndex);

  /**
   * Appends the provided AxisType to the current AxisType array
   * and creates corresponding length = 0 entry in the axis lengths
   * array.
   * 
   * @param imageIndex
   * @param type
   */
  void addAxis(int imageIndex, AxisType type);

  /**
   * Appends the provided AxisType to the current AxisType array
   * and creates a corresponding entry with the specified value in
   * axis lengths.
   * 
   * @param imageIndex
   * @param type
   * @param value
   */
  void addAxis(int imageIndex, AxisType type, int value);

  boolean isOrderCertain(int imageIndex);

  boolean isThumbnailImage(int imageIndex);

  boolean isMetadataComplete(int imageIndex);

  void putDatasetMeta(String key, Object value);

  void putImageMeta(int imageIndex, String key, Object value);

  void setThumbSizeX(int imageIndex, int thumbX);

  void setThumbSizeY(int imageIndex, int thumbY);

  void setPixelType(int imageIndex, int type);

  void setBitsPerPixel(int imageIndex, int bpp);

  void setChannelDimLengths(int imageIndex, int[] cLengths);

  void setChannelDimTypes(int imageIndex, String[] cTypes);

  void setOrderCertain(int imageIndex, boolean orderCertain);

  void setRGB(int imageIndex, boolean rgb);

  void setLittleEndian(int imageIndex, boolean littleEndian);

  void setInterleaved(int imageIndex, boolean interleaved);

  void setIndexed(int imageIndex, boolean indexed);

  void setFalseColor(int imageIndex, boolean falseC);

  void setMetadataComplete(int imageIndex, boolean metadataComplete);

  void setImageMetadata(int imageIndex, Hashtable<String, Object> meta);

  void setThumbnailImage(int imageIndex, boolean thumbnail);

  void setAxisTypes(int imageIndex, AxisType[] axisTypes);

  void setAxisType(int imageIndex, int axisIndex, AxisType axis);

  void setAxisLengths(int imageIndex, int[] axisLengths);

  void setAxisLength(int imageIndex, AxisType axis, int length);

  void resetMeta();

  Collection<M> getImageMetadata();

  void add(M meta);

}