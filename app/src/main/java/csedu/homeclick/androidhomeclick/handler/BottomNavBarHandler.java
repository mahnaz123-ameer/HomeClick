package csedu.homeclick.androidhomeclick.handler;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import org.jetbrains.annotations.NotNull;
//
//import csedu.homeclick.androidhomeclick.R;
//
//public class BottomNavBarHandler {
//    private static BottomNavBarHandler bottomNavBarHandler;
//    private static BottomNavigationView bottomNavigationView;
//    private static int selectedItem;
//
//    public static BottomNavigationView getBottomNavigationView() {
//        return bottomNavigationView;
//    }
//
//    private BottomNavBarHandler(View view, int selectIt) {
//        bottomNavigationView = view.findViewById(R.id.bottom_navigation_bar);
//        selectedItem = selectIt;
//        bottomNavigationView.setSelectedItemId(selectedItem);
//    }
//
//    public static void setInstance(View view, int selectIt) {
////        if(bottomNavBarHandler == null) {
//        bottomNavBarHandler = new BottomNavBarHandler(view, selectIt);
////        }
//    }
//
//    public static void handle(Context context) {
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home:
//                        Toast.makeText(context, "home selected", Toast.LENGTH_SHORT).show();
//                        context.startActivity(new Intent(context, AdFeed.class));
//                        break;
//                    case R.id.add:
//                        Toast.makeText(context, "add selected", Toast.LENGTH_SHORT).show();
//                        context.startActivity(new Intent(context, UserSignIn.class));
//                        break;
//                    case R.id.account:
//                        Toast.makeText(context, "account selected", Toast.LENGTH_SHORT).show();
//                        UserAuth.setInstance();
//                        if(UserAuth.isSignedIn()) {
//                            context.startActivity(new Intent(context, Profile.class));
//                        } else {
//                            context.startActivity(new Intent(context, UserSignIn.class));
//                        }
//
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
//
//        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
//            @Override
//            public void onNavigationItemReselected(@NonNull @NotNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        Toast.makeText(context, "home reselected", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_plus:
//                        Toast.makeText(context, "add reselected", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_account:
//                        Toast.makeText(context, "account reselected", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }
//
//}
