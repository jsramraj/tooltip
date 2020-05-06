package com.navram.tooltip;

import android.content.Context;

import com.navram.tooltip.utils.ResourceUtils;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ToolTipComposerTest {

    private static final String PAGE_NAME = "TestActivity";
    private static final String PAGE_COMPONENT_ID = "testResourceId";
    private static final String TIP_TITLE = "tipTitle";
    private static final String TIP_DESCRIPTION = "tipDescription";

    @Mock
    private Context context;

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
        toolTipModel.setPageName(PAGE_NAME);
        toolTipModel.setResourceId(PAGE_COMPONENT_ID);
        toolTipModel.setTitle(TIP_TITLE);
        toolTipModel.setMessage(TIP_DESCRIPTION);

        builder.addStaticTip(toolTipModel);
        ToolTipComposer tipComposer = builder.build();

        assertEquals(1, tipComposer.getAllTipData().size());

        List<ToolTipModel> testActivity = ResourceUtils.findToolTipModelItems(
                tipComposer.getAllTipData(), PAGE_NAME);

        assertNotNull(testActivity);
        assertFalse(testActivity.isEmpty());
        assertEquals(1, testActivity.size());
        assertEquals(PAGE_COMPONENT_ID, testActivity.get(0).getResourceId());
        assertEquals(TIP_TITLE, testActivity.get(0).getTitle());
        assertEquals(TIP_DESCRIPTION, testActivity.get(0).getMessage());
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

        List<ToolTipModel> values = ResourceUtils.findToolTipModelItems(tipComposer.getAllTipData(),
                "MainActivity");

        assertNotNull(values);
        assertFalse(values.isEmpty());
        assertEquals(3, values.size());
        assertEquals("helloWorldLabel", values.get(0).getResourceId());
    }

    @Test(expected = NullPointerException.class)
    public void builderCreation_WithNullJsonData_RaisesException() {
        ToolTipComposer.Builder builder = new ToolTipComposer.Builder();
        builder.addStaticTip(context, "");
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

        List<ToolTipModel> values = ResourceUtils.findToolTipModelItems(tipComposer.getAllTipData(),
                "MainActivity");

        assertNotNull(values);
        assertFalse(values.isEmpty());
        assertEquals(4, values.size());
        assertEquals("helloWorldLabel", values.get(0).getResourceId());
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
