package org.jcryptool.core.util.ui.layout;

import org.eclipse.swt.layout.GridData;

/**
 * Serve bonus features to the normal GridData, by providing a
 * fluent interface to minimalSize, sizeHint and Indents.
 * <p>
 * It can be used in a chain-like builder pattern, such as this:
 * <pre>
 * {@code
 * GridDataBuilder.empty().minimumWidth(100).horizontalIndent(16).get();
 * GridDataBuilder.with(SWT.FILL, SWT.TOP, false, false).heightHint(50).get();
 * }
* </pre>
* All overloaded constructors of {@link GridData} are available via these start methods:
* <ul>
*   <li>{@link #empty()}</li>
*   <li>{@link #with(int)}</li>
*   <li>{@link #with(int, int)}</li>
*   <li>{@link #with(int, int, boolean, boolean)}</li>
*   <li>{@link #with(int, int, boolean, boolean, int, int)}</li>
* </ul>
 */
public class GridDataBuilder {

    private GridData gridData;
    
    private GridDataBuilder(GridData gridData) {
        this.gridData = gridData;
    }
    
    /**
     * Create a new builder for a GridData object with no arguments.
     * Use methods such as {@link #verticalIndent(int)} or {@link #widthHint(int)} to extend it.
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#GridData()
     */
    public static GridDataBuilder empty() {
        return new GridDataBuilder(new GridData());
    }
    
    /**
     * Create a new builder for a GridData object with the style argument.
     * Use methods such as {@link #verticalIndent(int)} or {@link #widthHint(int)} to extend it.
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#GridData(int)
     */
    public static GridDataBuilder with(int style) {
        return new GridDataBuilder(new GridData(style));
    }
    
    /**
     * Create a new builder for a GridData object with the width/height argument.
     * Use methods such as {@link #verticalIndent(int)} or {@link #widthHint(int)} to extend it.
     * <p>
     * <b>Note:</b> The usage of this version of GridData is discouraged in JCrypTool.
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#GridData(int)
     */
    public static GridDataBuilder with(int width, int height) {
        return new GridDataBuilder(new GridData(width, height));
    }
    
    /**
     * Create a new builder for a GridData object with the alignment parameters.
     * Use methods such as {@link #verticalIndent(int)} or {@link #widthHint(int)} to extend it.
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#GridData(int, int, boolean, boolean)
     */
    public static GridDataBuilder with(
            int horizontalAlignment,
            int verticalAlignment,
            boolean grabExcessHorizontalSpace,
            boolean grabExcessVerticalSpace) {
        return new GridDataBuilder(new GridData(
                horizontalAlignment,
                verticalAlignment,
                grabExcessHorizontalSpace,
                grabExcessVerticalSpace));
    }
    
    /**
     * Create a new builder for a GridData object with the alignment and size parameters.
     * Use methods such as {@link #verticalIndent(int)} or {@link #widthHint(int)} to extend it.
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#GridData(int, int, boolean, boolean)
     */
    public static GridDataBuilder with(
            int horizontalAlignment,
            int verticalAlignment,
            boolean grabExcessHorizontalSpace,
            boolean grabExcessVerticalSpace,
            int horizontalSpan,
            int verticalSpan) {
        return new GridDataBuilder(new GridData(
                horizontalAlignment,
                verticalAlignment,
                grabExcessHorizontalSpace,
                grabExcessVerticalSpace,
                horizontalSpan,
                verticalSpan));
    }
    
    /**
     * Set the GridData's verticalIndent (cell's margin from top)
     * @param verticalIndent new value
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#verticalIndent
     */
    public GridDataBuilder verticalIndent(int verticalIndent) {
        gridData.verticalIndent = verticalIndent;
        return this;
    }
    
    /**
     * Set the GridData's horizontalIndent (cell's margin from left)
     * @param horizontalIndent new value
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#horizontalIndent
     */
    public GridDataBuilder horizontalIndent(int horizontalIndent) {
        gridData.horizontalIndent = horizontalIndent;
        return this;
    }
    
    /**
     * Set the GridData's widthHint (requested width of the widget - may be ignored).
     * @param widthHint new value
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#widthHint
     */
    public GridDataBuilder widthHint(int widthHint) {
        gridData.widthHint = widthHint;
        return this;
    }
    
    /**
     * Set the GridData's heightHint (requested height of the widget - may be ignored).
     * @param heightHint new value
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#heightHint
     */
    public GridDataBuilder heightHint(int heightHint) {
        gridData.heightHint = heightHint;
        return this;
    }
    
    /**
     * Set the GridData's minimumWidth (value below the widget will not be resized).
     * @param minimumWidth new value
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#minimumWidth
     */
    public GridDataBuilder minimumWidth(int minimumWidth) {
        gridData.minimumWidth = minimumWidth;
        return this;
    }
    
    /**
     * Set the GridData's minimumHeight (value below the widget will not be resized).
     * @param minimumHeight new value
     * @return itself in a fluent builder pattern. Use {@link #get()} to finish.
     * @see GridData#minimumHeight
     */
    public GridDataBuilder minimumHeight(int minimumHeight) {
        gridData.minimumHeight = minimumHeight;
        return this;
    }
    

    /**
     * Finalize the object creation.
     * @return A newly constructed GridData with the applied values.
     */
    public GridData get() {
        return gridData;
    }
}
