/*
 * Copyright (c) 2018, Jamie Mansfield <https://jamiemansfield.me/>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package me.jamiemansfield.bombe.asm.analysis;

import me.jamiemansfield.bombe.analysis.InheritanceProvider;
import me.jamiemansfield.bombe.asm.jar.ClassProvider;
import org.objectweb.asm.ClassReader;

import java.util.Optional;

/**
 * An implementation of {@link InheritanceProvider} that retrieves all of
 * its information from a {@link ClassProvider}.
 *
 * @author Jamie Mansfield
 * @since 0.3.0
 */
public class ClassProviderInheritanceProvider implements InheritanceProvider {

    private final ClassProvider provider;

    public ClassProviderInheritanceProvider(final ClassProvider provider) {
        this.provider = provider;
    }

    @Override
    public Optional<ClassInfo> provide(final String klass) {
        final byte[] classBytes = this.provider.get(klass);
        if (classBytes == null) return Optional.empty();

        final ClassReader reader = new ClassReader(classBytes);
        final InheritanceClassInfoVisitor classInfoVisitor = new InheritanceClassInfoVisitor();
        reader.accept(classInfoVisitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        return Optional.of(classInfoVisitor.create());
    }

}
