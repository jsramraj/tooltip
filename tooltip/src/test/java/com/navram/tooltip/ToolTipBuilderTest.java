package com.navram.tooltip;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ToolTipBuilderTest {

    @Mock
    Context context;
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor editor;

    @Mock
    ToolTipBuilder builder;

    @Before
    public void setup() {
        sharedPreferences = Mockito.mock(SharedPreferences.class);
        editor = Mockito.mock(SharedPreferences.Editor.class);
        context = Mockito.mock(Context.class);

        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.commit()).thenReturn(true);

        Map<String, String> map = new HashMap<>();

        when(sharedPreferences.getBoolean(anyString(), anyBoolean())).thenAnswer((Answer<Boolean>) invocation -> {
            String key = invocation.getArgument(0);
            return map.containsKey(key);
        });
        when(sharedPreferences.getAll()).thenAnswer((Answer<Map<String, String>>) invocation -> {
            Map<String, String> copy = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }
            return copy;
        });
        when(editor.putBoolean(anyString(), anyBoolean())).thenAnswer((Answer<SharedPreferences.Editor>) invocation -> {
            String key = invocation.getArgument(0);
            String value = invocation.getArgument(0);
            map.put(key, value);
            return null;
        });
        when(editor.remove(anyString())).thenAnswer((Answer<SharedPreferences.Editor>) invocation -> {
            Iterator<String> iterator = map.keySet().iterator();
            String key = invocation.getArgument(0);

            while (iterator.hasNext()) {
                String iter = iterator.next();
                if (iter.startsWith(key)) {
                    iterator.remove();
                }
            }
            return null;
        });

        PersistentManager.getInstance().init(context);
        builder = new ToolTipBuilder();
    }

    @Test
    public void creationOfTip_WorksCorrect() {
        builder.addStaticTip("MainActivity", 123, "Test tip title", "Test tip message");

        assertEquals(1, builder.staticTipsForActivity("MainActivity").size());
        assertEquals("Test tip message", builder.staticTipsForActivity("MainActivity").get(0).getTipText());
    }
}
