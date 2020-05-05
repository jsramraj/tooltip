package com.navram.tooltip;

import org.json.JSONException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ToolTipComposerTest {

    @Test
    public void toolTipComposerBuilder_returns_validTipComposer() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        ToolTipComposer tipComposer = builder.build();

        assertNotNull(tipComposer);
        assertThat(tipComposer, instanceOf(ToolTipComposer.class));
    }

    @Test
    public void toolTipComposerBuilder_Creation_Initialize_EmptyAllTipData() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        ToolTipComposer tipComposer = builder.build();

        assertNotNull(tipComposer.getAllTipData());
        assertTrue(tipComposer.getAllTipData().isEmpty());
    }

    @Test
    public void builderCreation_WithSingleTipData_ReturnesCorrectData() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();

        ToolTipModel toolTipModel = new ToolTipModel();
        toolTipModel.setPageName("TestActivity");
        toolTipModel.setComponentId("testResourceId");
        toolTipModel.setTitle("tipTitle");
        toolTipModel.setMessage("tipDescription");

        builder.addStaticTip(toolTipModel.getPageName(), toolTipModel);
        ToolTipComposer tipComposer = builder.build();

        assertEquals(1, tipComposer.getAllTipData().size());
        assertTrue(tipComposer.getAllTipData().containsKey("TestActivity"));
        assertEquals(1, tipComposer.getAllTipData().get("TestActivity").size());
        assertEquals("testResourceId", tipComposer.getAllTipData().get("TestActivity").get(0).getComponentId());
        assertEquals("tipTitle", tipComposer.getAllTipData().get("TestActivity").get(0).getTitle());
        assertEquals("tipDescription", tipComposer.getAllTipData().get("TestActivity").get(0).getMessage());
    }

    @Test(expected = NullPointerException.class)
    public void builderCreation_WithNullData_RaisesException() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.addStaticTips("MainActivity", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderCreation_WithInvalidData_RaisesException() {
        String[] identifiers = new String[]{"helloWorldLabel", "empNameTextView"};//two object
        String[] tips = new String[]{"Tips for hello world"};//one object

        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.addStaticTips("MainActivity", identifiers, tips, null);
    }

    @Test
    public void builderCreation_WithMultipleTipData_ReturnesCorrectData() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        String[] identifiers = new String[]{"helloWorldLabel", "empNameTextView", "designationTextView"};
        String[] tips = new String[]{"Tips for hello world", "Name of the logged in employee", "Role of the employee in the company"};
        builder.addStaticTips("MainActivity", identifiers, tips, null);
        ToolTipComposer tipComposer = builder.build();

        assertEquals(1, tipComposer.getAllTipData().size());
        assertTrue(tipComposer.getAllTipData().containsKey("MainActivity"));
        assertEquals(3, tipComposer.getAllTipData().get("MainActivity").size());

        assertEquals("helloWorldLabel", tipComposer.getAllTipData().get("MainActivity").get(0).getComponentId());
    }

    @Test(expected = NullPointerException.class)
    public void builderCreation_WithNullJsonData_RaisesException() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.addStaticTip(null, "");
    }

    @Test(expected = JSONException.class)
    public void builderCreation_WithInvalidJsonData_RaisesException() throws JSONException {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.addStaticTip("null : 123");
    }

    @Test(expected = JSONException.class)
    public void builderCreation_WithInvalidJsonData_RaisesException2() throws JSONException {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.addStaticTip("[\"test\" : 123]");
    }

    @Test
    public void builderCreation_WithValidJsonData_ReturnesCorrectData() throws JSONException {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        String jsonData = "{\n" +
                "  \"MainActivity\": [\n" +
                "    {\n" +
                "      \"id\": \"helloWorldLabel\",\n" +
                "      \"message\": \"Tips for hello world\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"empName\",\n" +
                "      \"message\": \"Name of the logged in employee\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"designation\",\n" +
                "      \"message\": \"Role of the employee in the company\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"otherDetailsTextView\",\n" +
                "      \"message\": \"Some other details about the employee\"\n" +
                "    }\n" +
                "  ]}";
        builder.addStaticTip(jsonData);
        ToolTipComposer tipComposer = builder.build();

        assertEquals(1, tipComposer.getAllTipData().size());
        assertTrue(tipComposer.getAllTipData().containsKey("MainActivity"));
        assertEquals(4, tipComposer.getAllTipData().get("MainActivity").size());

        assertEquals("helloWorldLabel", tipComposer.getAllTipData().get("MainActivity").get(0).getComponentId());
    }

    @Test
    public void settingNullConfig_Doesnt_Crash() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.setGlobalConfig(null);
        assertNotNull(ToolTipConfig.getInstance());
    }

    @Test
    public void settingValidConfig_SetsUpTheGlobalConfig_Correct() {
        ToolTipConfig toolTipConfig = new ToolTipConfig();
        toolTipConfig.setTipMessageStyleResId(1);
        toolTipConfig.setTipTitleStyleResId(2);

        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.setGlobalConfig(toolTipConfig);
        builder.build();

        assertNotNull(ToolTipConfig.getInstance());
        assertEquals(1, ToolTipConfig.getInstance().getTipMessageStyleResId());
        assertEquals(2, ToolTipConfig.getInstance().getTipTitleStyleResId());
    }

}
