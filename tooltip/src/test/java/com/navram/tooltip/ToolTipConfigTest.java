package com.navram.tooltip;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ToolTipConfigTest {
    @Test
    public void singleton_Returns_ValidInstance() {
        assertNotNull(ToolTipConfig.getInstance());
    }

    @Test
    public void instanceInjection_ReplacesTheOldInstance() {
        ToolTipConfig instance = Mockito.mock(ToolTipConfig.class);
        ToolTipConfig.setInstance(instance);

        assertEquals(instance, ToolTipConfig.getInstance());
    }

    @Test
    public void settingStyleConfig_WorksCorrect() {
        ToolTipConfig instance = new ToolTipConfig();
        instance.setTipTitleStyleResId(1);
        instance.setTipMessageStyleResId(2);

        ToolTipConfig.setInstance(instance);

        assertEquals(1, ToolTipConfig.getInstance().getTipTitleStyleResId());
        assertEquals(2, ToolTipConfig.getInstance().getTipMessageStyleResId());
    }

}
