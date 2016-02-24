# ViewDragHelper
一个简单的ViewDragHelper示例
# ViewDragHelper类

参考文章:[张鸿洋博客](http://blog.csdn.net/lmj623565791/article/details/46858663)

## 基础使用

1. 创建实例
    自定义ViewGroup
    ```java
    public class VDHLayout extends LinearLayout {

        private ViewDragHelper mDragger;

        public VDHLayout(Context context) {
            this(context, null);
        }

        public VDHLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public VDHLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initViewDragHelper();
        }

        private void initViewDragHelper() {
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
            });
        }
    }
    ````
    在创建实例时需要传入3个参数，
    1. 当前ViewGroup
    2. sensitivity，主要用于设置touchSlop
    ```java
    helper.mTouchSlop = (int) (helper.mTouchSlop * (1 / sensitivity));
    ```
    sensitivity越大，mTouchSlop越小
    3. Callback，触摸过程中的回调方法

2. 触摸相关方法

    ```java
    @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            //决定是否应当拦截当前的事件
            return mDragger.shouldInterceptTouchEvent(ev);
        }

    @Override
        public boolean onTouchEvent(MotionEvent event) {
            //onTouchEvent中通过mDragger.processTouchEvent(event)处理事件
            mDragger.processTouchEvent(event);
            return true;
        }
    ```

3. 实现callback方法
    ```java
    new ViewDragHelper.Callback()
            {
                @Override
                public boolean tryCaptureView(View child, int pointerId)
                {
                    return true;
                }

                @Override
                public int clampViewPositionHorizontal(View child, int left, int dx)
                {
                    return left;
                }

                @Override
                public int clampViewPositionVertical(View child, int top, int dy)
                {
                    return top;
                }
            }
    ```
    1. tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
    2. clampViewPositionHorizontal,clampViewPositionVertical可以在该方法中对child移动的边界进行控制，left , top 分别为即将移动到的位置

### 改造

1. 改造的结果：
  * 第一个view正常拖动
  * 第二个view拖动后手指释放时回到之前的位置
  * 边缘拖动

2. 对于view1的改造：

    对于view1改造的不多，只需要在tryCaptureView函数中判断当前是否是拖动view1

3. 对于view2的改造：
    * 因为释放后view2要回到原来的位置，所以需要在onLayout函数中记录下view的left和top位置
    * 重写callback中的onViewReleased判断如果是view2则调用settleCapturedViewAt回到初始位置。
4.  对于view3的改造
    * 重写onEdgeDragStarted

### 补充
