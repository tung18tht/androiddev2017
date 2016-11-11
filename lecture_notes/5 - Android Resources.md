# Android Resources

### Group 5
* Nguyen Duc Tung - USTHBI5 - 146
* Dang Vu Lam - USTHBI4 - 078
* Do Dang Ngoc Kha - USTHBI5 - 061

### Chapter Objective

Learn about different resources, how to use them in an Android application

#### Basic concepts:

* Resources are things embedded (bundled) into the app
* Stored in `res/` directory
* Accessible through code: `R.<category>.<resourceName>`

## I. Layouts

**Definition:**

* ViewGroup is the base View class for layouting in Android • Layout is a way to organise Views
* Can be created by code or XML files in res/layout
* Has hierarchical structure and can be nested

**Layout XML:**

* Containers (ViewGroups) contain Views, required layout_width, layout_height
* Adaptive Layout: Use different layout XMLs in different directories
  * Tablet: layout-large, layout-xlarge
  * Phone: layout-normal
  * Small: layout-small
  * Orientation: -land, -port

* Example loading XML layout:
```java
// Activity
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_layout);
}
// Fragment
public View onCreateView(LayoutInflater inflater,
                         ViewGroup container,...) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_layout,
                            container, false);
}
```

**Popular Layout classes:**

* FrameLayout
  * Can contain multiple children (Views)
  * Multiple layers, z-based order. First child at the bottom

* LinearLayout
  * One direction (Horizontal or Vertical)
  * Use layout-weight for stretching based on orientation

* RelativeLayout
  * Multiple layers, z-based order
  * Relativity of children’s position and size to parent or others
