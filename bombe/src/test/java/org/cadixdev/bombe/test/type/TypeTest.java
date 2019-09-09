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

package org.cadixdev.bombe.test.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cadixdev.bombe.type.ArrayType;
import org.cadixdev.bombe.type.BaseType;
import org.cadixdev.bombe.type.FieldType;
import org.cadixdev.bombe.type.ObjectType;
import org.cadixdev.bombe.type.Type;
import org.cadixdev.bombe.type.VoidType;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests pertaining to the type model in Bombe.
 */
public final class TypeTest {

    private static final Map<Class<?>, BaseType> BASE_MAPPINGS = Collections.unmodifiableMap(new HashMap<Class<?>, BaseType>() {
        {
            this.put(Boolean.TYPE, BaseType.BOOLEAN);
            this.put(Character.TYPE, BaseType.CHAR);
            this.put(Byte.TYPE, BaseType.BYTE);
            this.put(Short.TYPE, BaseType.SHORT);
            this.put(Integer.TYPE, BaseType.INT);
            this.put(Long.TYPE, BaseType.LONG);
            this.put(Float.TYPE, BaseType.FLOAT);
            this.put(Double.TYPE, BaseType.DOUBLE);
        }
    });

    @Test
    public void arrayType() {
        final String raw = "[[I";
        final Type type = Type.of(raw);
        assertTrue(type instanceof ArrayType, "Type should be an ArrayType!");
        assertEquals(raw, type.toString());
        final ArrayType array = (ArrayType) type;
        assertEquals(2, array.getDimCount());
        assertEquals(BaseType.INT, array.getComponent());
    }

    @Test
    public void objectType() {
        final String raw = "Lme/jamiemansfield/Test;";
        final Type type = Type.of(raw);
        assertTrue(type instanceof ObjectType, "Type should be an ObjectType!");
        assertEquals(raw, type.toString());
    }

    @Test
    public void baseType() {
        final String raw = "Z";
        final Type type = Type.of(raw);
        assertTrue(type instanceof BaseType, "Type should be an BaseType!");
        assertEquals(BaseType.BOOLEAN, type);
        assertEquals(raw, type.toString());
    }

    @Test
    public void voidType() {
        final String raw = "V";
        final Type type = Type.of(raw);
        assertTrue(type instanceof VoidType, "Type should be an VoidType!");
        assertEquals(VoidType.INSTANCE, type);
        assertEquals(raw, type.toString());
    }

    @Test
    public void invalidType() {
        assertThrows(IllegalStateException.class, () -> Type.of("A"));
        assertThrows(IllegalStateException.class, () -> FieldType.of("V"));
    }

    @Test
    public void ofClass() {
        final Type stringType = Type.of(String.class);
        assertTrue(stringType instanceof ObjectType, "Type should be an ObjectType!");
        assertEquals("Ljava/lang/String;", stringType.toString());

        BASE_MAPPINGS.forEach((klass, type) -> {
            final Type baseType = Type.of(klass);
            assertTrue(baseType instanceof BaseType, "Type should be an BaseType!");
            assertEquals(type, baseType);
        });

        final Type voidType = Type.of(Void.TYPE);
        assertTrue(voidType instanceof VoidType, "Type should be an VoidType!");
        assertEquals(VoidType.INSTANCE, voidType);
    }

    @Test
    public void normaliseClass() {
        final ObjectType test = new ObjectType("java.lang.String");
        assertEquals("java/lang/String", test.getClassName());
    }

}