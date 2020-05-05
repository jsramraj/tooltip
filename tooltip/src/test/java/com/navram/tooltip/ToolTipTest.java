package com.navram.tooltip;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToolTipTest {
    @Test
    public void verifyStaticToolTipCreation() {
        StaticTip staticTip = new StaticTip("MainActivity", 123, "Tip title", "Tip message");

        assertEquals("MainActivity", staticTip.getActivityName());
        assertEquals(123, staticTip.getResourceId());
        assertEquals("Tip message", staticTip.getTipText());
        assertEquals("Tip title", staticTip.getTipTitle());
    }
}
