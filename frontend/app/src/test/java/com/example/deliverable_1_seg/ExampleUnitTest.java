package com.example.deliverable_1_seg;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.deliverable_1_seg.helpers.DataValidator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void InvalidPassword() {
        assertFalse(DataValidator.signUpPassword("pass"));
    }

    @Test
    public void EmptyPassword() {
        assertFalse(DataValidator.signUpPassword(""));
    }

    @Test
    public void NullPassword() {
        assertFalse(DataValidator.signUpPassword(null));
    }


    @Test
    public void ValidPassword() {
        assertTrue(DataValidator.signUpPassword("Password1!"));
    }
}