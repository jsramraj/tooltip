package com.navram.tooltip;

import com.navram.tooltip.utils.GeometryUtils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GeometryUtilsTest {
    @Test
    public void checkIf_isSquare_ReturnsFalseForRectangles() {
        assertFalse(GeometryUtils.isAlmostSquare(100, 200, 0.1f));
        assertFalse(GeometryUtils.isAlmostSquare(100, 110, 0f));
    }

    @Test
    public void checkIf_isSquare_ReturnsTrueForRectangles() {
        assertTrue(GeometryUtils.isAlmostSquare(100, 100, 0.1f));
        assertTrue(GeometryUtils.isAlmostSquare(100, 110, 0.1f));
    }
}
