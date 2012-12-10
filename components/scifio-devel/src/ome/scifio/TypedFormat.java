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

/**
 * TODO
 * @author Mark Hiner
 *
 * @param <M>
 * @param <C>
 * @param <P>
 * @param <R>
 * @param <W>
 */
public interface TypedFormat<M extends TypedMetadata, C extends Checker,
P extends TypedParser<M>, R extends TypedReader<M, DataPlane<?>>,
W extends TypedWriter<M>> extends Format {

  /**
   * Create the Metadata object associated with this format.
   * @return
   * @throws FormatException
   */
  M createMetadata() throws FormatException;

  /**
   * Create the Checker object associated with this format.
   * @return
   * @throws FormatException
   */
  C createChecker() throws FormatException;

  /**
   * Create the Parser object associated with this format.
   * @return
   * @throws FormatException
   */
  P createParser() throws FormatException;

  /**
   * Creates the Reader object associated with this format.
   * @return
   * @throws FormatException
   */
  R createReader() throws FormatException;

  /**
   * Creates the Writer object associated with this format.
   * @return
   * @throws FormatException
   */
  W createWriter() throws FormatException;

  /**
   * Returns a translator capable of translating from this format's Metadata
   * to the target Metadata class. (this format's Metadata is the source)
   * 
   * @param <N>
   * @param targetMeta
   * @return
   */
  <N extends TypedMetadata> TypedTranslator<M, N> findSourceTranslator(N targetMeta)
    throws FormatException;

  /**
   * Returns a translator capable of translating from the target Metadata to this
   * format's Metadata (this Format's metadata is the destination).
   * 
   * @param <N>
   * @param targetMeta
   * @return
   */
  <N extends TypedMetadata> TypedTranslator<N, M> findDestTranslator(N targetMeta)
    throws FormatException;
  
  /**
   * Returns the class of the Metadata associated with this format
   * @return
   */
  Class<M> getMetadataClass();

  /**
   * Returns the class of the Checker associated with this format
   * @return
   */
  Class<C> getCheckerClass();

  /**
   * Returns the class of the Parser associated with this format
   * @return
   */
  Class<P> getParserClass();

  /**
   * Returns the class of the Reader associated with this format
   * @return
   */
  Class<R> getReaderClass();

  /**
   * Returns the class of the Writer associated with this format
   * @return
   */
  Class<W> getWriterClass();
}
