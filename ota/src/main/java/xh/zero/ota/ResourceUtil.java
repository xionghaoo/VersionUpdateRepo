/*
 *   Copyright 2012 Hai Bison
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package xh.zero.ota;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class ResourceUtil {

    private ResourceUtil() {
        throw new AssertionError("You can not instantiate this class. Use its static utility " +
                "methods instead");
    }

    /**
     * Get color from a resource id
     *
     * @param context  The context
     * @param colorRes The resource identifier of the color
     * @return The resolved color value
     */
    public static int getColor(@NonNull Context context, @ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }

    /**
     * Get string from a resource id
     *
     * @param context   The context
     * @param stringRes The resource identifier of the string
     * @return The string value
     */
    public static String getString(@NonNull Context context, @StringRes int stringRes) {
        return context.getString(stringRes);
    }

    /**
     * Get dimension in pixels from its resource id
     *
     * @param context  The context
     * @param dimenRes The resource identifier of the dimension
     * @return The dimension in pixels
     */
    public static float getDimensionInPx(@NonNull Context context, @DimenRes int dimenRes) {
        return context.getResources().getDimension(dimenRes);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
