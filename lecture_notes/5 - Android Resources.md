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

*ViewPager*

* Tab-like container, horizontal swipe scrolling gesture

* No header, use `TabLayout` for that

* Each tab is a fragment
  * Can be nested
  * **Off screen** limit: protect resources

* `Adapter`
  * Specify what fragment in what page

  ```java
  // Adapter example
  public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
      private final int PAGE_COUNT = 3;
      private String titles[] = new String[] { "Hanoi", "Paris", "Toulouse" };

      public HomeFragmentPagerAdapter(FragmentManager fm) {
          super(fm);
      }

      @Override
      public int getCount() {
          return PAGE_COUNT;
      }

      @Override
      public Fragment getItem(int page) {
      // returns an instance of Fragment corresponding to the specified page
          switch (page) {
              case 0: return Fragment1.newInstance();
              case 1: return Fragment2.newInstance();
              case 2: return Fragment3.newInstance();
          }
          return new EmptyFragment(); // failsafe
      }

      @Override
      public CharSequence getPageTitle(int page) {
          // returns a tab title corresponding to the specified page
          return titles[position];
      }
  }
  ```

  * Setup `ViewPager` in Activity's `onCreate()`
  ```java
  PagerAdapter adapter = new HomeFragmentPagerAdapter( getSupportFragmentManager());
  pager.setAdapter(adapter);
  ```

* `TabLayout`
  * `build.gradle` of the app:
  ```compile "com.android.support:design:23.1.0"```

  * In Activity or Fragment: setup a link between them
  ```tabLayout.setupWithViewPager(pager);```

2. Values

3. Drawables
