package android.tictactoecustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.lang.Integer.max
import kotlin.math.min
import kotlin.properties.Delegates

class TicTacToeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TicTacToeFieldStyle,
    defStyleRes: Int = R.style.DefaultTicTacToeFieldStyle
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    var ticTacToeField: TicTacToeField? = null
        set(value) {
            field?.listeners?.remove(listener)
            field = value
            value?.listeners?.add(listener)
            updateSizes()
            requestLayout()
            invalidate()
        }

    private var player1Color by Delegates.notNull<Int>() //by Delegates.notNull<Int>() т.к. цвет примитив
    private var player2Color by Delegates.notNull<Int>()
    private var greedColor by Delegates.notNull<Int>()

    private val fieldRect = Rect(0, 0, 0, 0)
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f

    private val listener: OnFieldChangedListener = {
    }

    init {
        if (attributeSet != null) {
            initAttributes(attributeSet, defStyleAttr, defStyleRes)
        } else {
            initDefaultColors()
        }
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TicTacToeView, defStyleAttr, defStyleRes)
        player1Color = typedArray.getColor(R.styleable.TicTacToeView_player1Color, PLAYER1_DEFAULT_COLOR)
        player2Color = typedArray.getColor(R.styleable.TicTacToeView_player2Color, PLAYER2_DEFAULT_COLOR)
        greedColor = typedArray.getColor(R.styleable.TicTacToeView_greedColor, GREED_DEFAULT_COLOR)
        typedArray.recycle()
    }

    private fun initDefaultColors() {
        player1Color = PLAYER1_DEFAULT_COLOR
        player2Color = PLAYER2_DEFAULT_COLOR
        greedColor = GREED_DEFAULT_COLOR
    }

    private fun updateSizes() {
        val field = this.ticTacToeField ?: return

        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        val cellWidht = safeWidth / field.columns.toFloat()
        val cellHeight = safeHeight / field.rows.toFloat()

        cellSize = min(cellWidht, cellHeight)
        cellPadding = cellSize * TWENTY_PERCENT

        val fieldWidht = cellSize * field.columns
        val fieldHeight = cellSize * field.rows

        fieldRect.left = (paddingLeft + (safeWidth - fieldWidht) / 2).toInt()
        fieldRect.top = (paddingTop + (safeHeight - fieldHeight) / 2).toInt()
        fieldRect.right = (fieldRect.left + fieldWidht).toInt()
        fieldRect.bottom = (fieldRect.top + fieldHeight).toInt()
    }

    override fun onAttachedToWindow() { //означает что вьюшка присоединена и может взаимодействовать с иерархией компонентов
        //тут обычно только инициализируют что-то
        ticTacToeField?.listeners?.add(listener)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        ticTacToeField?.listeners?.remove(listener)
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, desiredHeight: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val cellSizePixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DESIRED_CELL_SIZE,
            resources.displayMetrics
        ).toInt()

        val rows = ticTacToeField?.rows ?: 0
        val columns = ticTacToeField?.columns ?: 0

        val desiredWidth = max(minWidth, columns * cellSizePixels + paddingLeft + paddingRight)
        val desiredHeight = max(minHeight, rows * cellSizePixels + paddingTop + paddingBottom)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, desiredHeight),
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateSizes()
    }

    companion object {
        const val PLAYER1_DEFAULT_COLOR = Color.GREEN
        const val PLAYER2_DEFAULT_COLOR = Color.RED
        const val GREED_DEFAULT_COLOR = Color.LTGRAY

        const val DESIRED_CELL_SIZE = 50f
        const val TWENTY_PERCENT = 0.2f
    }
}