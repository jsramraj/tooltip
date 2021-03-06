package com.navram.tooltip;

import android.content.Context;

import androidx.annotation.NonNull;

import com.navram.tooltip.utils.ResourceUtils;
import com.navram.tooltip.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.navram.tooltip.Constants.RESOURCE_ID_KEY;
import static com.navram.tooltip.Constants.TIP_MESSAGE_ID_KEY;
import static com.navram.tooltip.Constants.TIP_TITLE_ID_KEY;

/**
 * Acts as an data source for the tooltip
 * Primary entry point for the tool tip library
 * This class stores the necessary information to construct the tip
 */
public class ToolTipComposer {

    private final List<ToolTipModel> allTipData;

    /**
     * Default constructor
     * Stores the resource id, title and text of the tip
     *
     * @param allTipData   All the trip data with key being the activity name
     * @param globalConfig App wide configuration for constructing the tips
     */
    private ToolTipComposer(List<ToolTipModel> allTipData,
                            ToolTipConfig globalConfig) {
        this.allTipData = allTipData;
        ToolTipConfig.setInstance(globalConfig);
    }

    public List<ToolTipModel> getAllTipData() {
        return allTipData;
    }

    /**
     * Helper class to create the TipComposer object
     */
    public static class Builder {

        private List<ToolTipModel> allTipData;

        private ToolTipConfig globalConfig;

        /**
         * Default constructor
         */
        public Builder() {
            allTipData = new ArrayList<>();
        }

        /**
         * Add static tip data
         *
         * @param activityName Local class name of the activity
         * @param identifiers  Array of resource id
         * @param titles       Array of titles for the tip
         * @param messages     Array of the tip message
         * @return A {@code ToolTipComposer.Builder} object
         */
        public Builder addStaticTips(String activityName, String[] identifiers, String[] titles,
                                     String[] messages) {
            if (activityName == null || identifiers == null || titles == null) {
                throw new NullPointerException("Missing mandatory values.");
            }
            if (identifiers.length != titles.length) {
                throw new IllegalArgumentException("identifiers and titles should have the same number of objects");
            }
            for (int i = 0; i < identifiers.length; i++) {
                String message = (messages == null || messages.length == 0) ? "" : messages[i];

                ToolTipModel toolTipModel = new ToolTipModel();
                toolTipModel.setPageName(activityName);
                toolTipModel.setResourceId(identifiers[i]);
                toolTipModel.setTitle(titles[i]);
                toolTipModel.setMessage(message);

                this.addStaticTip(toolTipModel);
            }
            return this;
        }

        /**
         * Add static tip data
         *
         * @param aToolTipModel hold the tooltip information
         */
        void addStaticTip(ToolTipModel aToolTipModel) {

            if (allTipData == null) {
                allTipData = new ArrayList<>();
            }

            allTipData.add(aToolTipModel);
        }

        /**
         * Construct the tip data from the json formatted tip data
         *
         * @param tipDataJson Tip data in Json format
         * @throws JSONException if the json is not following standard mentioned in the above link. Check Sample data section.
         * @see <a href="https://raw.githubusercontent.com/jsramraj/tooltip/master/app/src/main/assets/tooltip_data.json">Sample data</a>
         */
        public void addStaticTip(String tipDataJson) throws JSONException {

            if (tipDataJson == null)
                throw new NullPointerException("Json value cannot be null");

            JSONObject jsonData = new JSONObject(tipDataJson);
            for (Iterator<String> it = jsonData.keys(); it.hasNext(); ) {
                String activityName = it.next();
                JSONArray data = jsonData.getJSONArray(activityName);

                for (int i = 0; i < data.length(); i++) {

                    JSONObject tipObject = data.getJSONObject(i);

                    ToolTipModel toolTipModel = new ToolTipModel();
                    toolTipModel.setPageName(activityName);
                    toolTipModel.setResourceId(tipObject.getString(RESOURCE_ID_KEY));
                    toolTipModel.setTitle(tipObject.has(TIP_TITLE_ID_KEY) ? tipObject.getString(TIP_TITLE_ID_KEY) : "");
                    toolTipModel.setMessage(tipObject.getString(TIP_MESSAGE_ID_KEY));

                    this.addStaticTip(
                            toolTipModel
                    );
                }
            }
        }

        /**
         * Method to read json file from assets folder to create the static tool tip items from json
         * file. This will add available items to the tool tip composer.
         *
         * @param aContext      to read json file from assets folder
         * @param aJsonFileName json file name to read from assets {@link android.content.res.AssetManager}
         * @return tooltip composer builder object
         */
        public Builder addStaticTip(@NonNull Context aContext, @NonNull String aJsonFileName) {

            String jsonContent = ResourceUtils.readJSONFromAsset(aContext, aJsonFileName);

            try {

                if (StringUtils.isNullOrEmpty(jsonContent)) {
                    throw new IllegalArgumentException("File not available");
                }

                addStaticTip(jsonContent);
            } catch (JSONException e) {
                throw new IllegalArgumentException(
                        "Please provide a valid json file. " +
                                "Please take a look on this sample " +
                                "https://raw.githubusercontent.com/jsramraj/tooltip/master/app/src/main/assets/tooltip_data.json"
                );
            }
            return this;
        }

        /**
         * Set the app wide configuration to customize the appearance of the tip
         *
         * @param globalConfig to super impose style values
         * @return A {@code ToolTipComposer.Builder} object
         */
        public Builder setGlobalConfig(ToolTipConfig globalConfig) {
            this.globalConfig = globalConfig;
            return this;
        }

        /**
         * Create the tooltip composer object from all the tip data and info we have
         *
         * @return A new instance of {@code ToolTipComposer} class
         */
        public ToolTipComposer build() {
            return new ToolTipComposer(allTipData, globalConfig);
        }
    }
}
