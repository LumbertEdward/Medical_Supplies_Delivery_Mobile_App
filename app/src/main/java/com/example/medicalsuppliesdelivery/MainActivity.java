package com.example.medicalsuppliesdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalsuppliesdelivery.ActivitiesClasses.Login;
import com.example.medicalsuppliesdelivery.ActivitiesClasses.MapsActivity;
import com.example.medicalsuppliesdelivery.Adapters.CartAdapter;
import com.example.medicalsuppliesdelivery.Adapters.NewArrivalsAdapter;
import com.example.medicalsuppliesdelivery.Adapters.NotificationAdapter;
import com.example.medicalsuppliesdelivery.DataClasses.Companies;
import com.example.medicalsuppliesdelivery.DataClasses.FragmentClass;
import com.example.medicalsuppliesdelivery.DataClasses.Orders;
import com.example.medicalsuppliesdelivery.DataClasses.Products;
import com.example.medicalsuppliesdelivery.FragmentClasses.Account;
import com.example.medicalsuppliesdelivery.FragmentClasses.AdminPanel;
import com.example.medicalsuppliesdelivery.FragmentClasses.Cart;
import com.example.medicalsuppliesdelivery.FragmentClasses.CategoriesFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.CompaniesFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.DetailsFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.FavoritesFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.LaboratoryFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.MaternityFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.MpesaFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.NewArrivalsFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.NotificationsFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.OrdersFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.PayOnDeliveryFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.PaypalFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.PopularFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.SearchFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.SterilizationFragment;
import com.example.medicalsuppliesdelivery.FragmentClasses.SurgeryFragment;
import com.example.medicalsuppliesdelivery.Interfaces.AdminInterface;
import com.example.medicalsuppliesdelivery.Interfaces.SuppliesInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements SuppliesInterface {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private TextView userE;
    private TextView heading;
    private CircleImageView circleImageView;
    //private TextView mLocation;
    private CoordinatorLayout coordinatorLayout;
    //Fragments
    private CompaniesFragment companiesFragment;
    private OrdersFragment ordersFragment;
    private Cart cart;
    private Account account;
    private CategoriesFragment categoriesFragment;
    private DetailsFragment detailsFragment;
    private AdminPanel panel;
    private SearchFragment searchFragment;
    private LaboratoryFragment laboratoryFragment;
    private SurgeryFragment surgeryFragment;
    private MaternityFragment maternityFragment;
    private SterilizationFragment sterilizationFragment;
    private NewArrivalsFragment newArrivalsFragment;
    private PopularFragment popularFragment;
    private FavoritesFragment favoritesFragment;
    private MpesaFragment mpesaFragment;
    private PaypalFragment paypalFragment;
    private PayOnDeliveryFragment payOnDeliveryFragment;
    private NotificationsFragment notificationsFragment;

    private ArrayList<FragmentClass> fragmentClasses = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private int mCount = 0;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private static final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.bl));
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();

        init();
        navInit();
        bottomInit();
        setHeader();
        setHeaderImage();
        setLocation();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)){

            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE);
            }
        }
        else {

        }
    }

    private void setHeaderImage() {
        if (auth.getCurrentUser() != null){
            reference = database.getReference().child("Users").child(auth.getUid()).child("userImage");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String url = snapshot.getValue().toString();
                        Picasso.Builder builder = new Picasso.Builder(MainActivity.this);
                        builder.downloader(new OkHttp3Downloader(MainActivity.this));
                        builder.build().load(url).into(circleImageView);
                    }
                    else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void setLocation() {
        if (auth.getCurrentUser() != null){
            reference = database.getReference().child("Users").child(auth.getUid()).child("location");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String loc = snapshot.getValue().toString();
                        //mLocation.setText(loc);
                    }
                    else {
                        //mLocation.setText("Select Location");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void setHeader() {
        View nav = navigationView.getHeaderView(0);
        userE = (TextView) nav.findViewById(R.id.emailUser);
        circleImageView = (CircleImageView) nav.findViewById(R.id.profilePic);

        if (auth.getCurrentUser() != null){
            reference = database.getReference().child("Users").child(auth.getUid()).child("email");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String email = snapshot.getValue().toString();
                        userE.setText(email);
                    }
                    else {
                        userE.setText("Email");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void init() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolMain);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorMed);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        //mLocation = (TextView) findViewById(R.id.location);
        heading = (TextView) findViewById(R.id.header);
        heading.setText("MEDISUPP");
        navigationView = (NavigationView) findViewById(R.id.navigation);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_format_align_left_black_24dp);

        if (companiesFragment == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            companiesFragment = new CompaniesFragment();
            fragmentTransaction.add(R.id.frame, companiesFragment, "Companies");
            tags.add("Companies");
            fragmentClasses.add(new FragmentClass(companiesFragment, "Companies"));
            fragmentTransaction.commit();
        }
        else {
            tags.remove("Companies");
            tags.add("Companies");
        }
        setVisibility("Companies");


    }
    public void removeBadge(){
        BottomNavigationItemView botView = bottomNavigationView.findViewById(R.id.cart);
        if (botView.getChildCount() == 3){
            botView.removeViewAt(1);
        }
    }

    private void bottomInit() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.comps:
                        //toolbar.setTitle("Companies");
                        if (companiesFragment == null){
                            companiesFragment = new CompaniesFragment();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, companiesFragment, "Companies");
                            tags.add("Companies");
                            fragmentClasses.add(new FragmentClass(companiesFragment, "Companies"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Companies");
                            tags.add("Companies");
                        }
                        setVisibility("Companies");
                        break;
                    case R.id.search:
                        if (searchFragment == null){
                            searchFragment = new SearchFragment();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, searchFragment, "Search");
                            tags.add("Search");
                            fragmentClasses.add(new FragmentClass(searchFragment, "Search"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Search");
                            tags.add("Search");
                        }
                        setVisibility("Search");
                        break;
                    case R.id.cart:
                        //toolbar.setTitle("Cart");
                        if (cart == null){
                            cart = new Cart();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, cart, "Cart");
                            tags.add("Cart");
                            fragmentClasses.add(new FragmentClass(cart, "Cart"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Cart");
                            tags.add("Cart");
                        }
                        setVisibility("Cart");
                        break;
                    case R.id.user:
                        //toolbar.setTitle("My Profile");
                        if (account == null){
                            account = new Account();
                            transaction.setCustomAnimations(R.anim.slide_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, account, "Account");
                            tags.add("Account");
                            fragmentClasses.add(new FragmentClass(account, "Account"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Account");
                            tags.add("Account");
                        }
                        setVisibility("Account");
                        break;
                }
                return true;
            }
        });
    }

    private void navInit() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.companies:
                        //toolbar.setTitle("Companies");
                        if (companiesFragment == null){
                            companiesFragment = new CompaniesFragment();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, companiesFragment, "Companies");
                            tags.add("Companies");
                            fragmentClasses.add(new FragmentClass(companiesFragment, "Companies"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Companies");
                            tags.add("Companies");
                        }
                        setVisibility("Companies");
                        break;
                    case R.id.myOrders:
                        //toolbar.setTitle("Orders");
                        if (ordersFragment == null){
                            ordersFragment = new OrdersFragment();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, ordersFragment, "Orders");
                            tags.add("Orders");
                            fragmentClasses.add(new FragmentClass(ordersFragment, "Orders"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Orders");
                            tags.add("Orders");
                        }
                        setVisibility("Orders");
                        break;
                    case R.id.cartNav:
                        if (cart == null){
                            cart = new Cart();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, cart, "Cart");
                            tags.add("Cart");
                            fragmentClasses.add(new FragmentClass(cart, "Cart"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Cart");
                            tags.add("Cart");
                        }
                        setVisibility("Cart");
                        break;
                    case R.id.favorites:
                        if (favoritesFragment == null){
                            favoritesFragment = new FavoritesFragment();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, favoritesFragment, "Favorites");
                            tags.add("Favorites");
                            fragmentClasses.add(new FragmentClass(favoritesFragment, "Favorites"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Favorites");
                            tags.add("Favorites");
                        }
                        setVisibility("Favorites");
                        break;
                    case R.id.logOut:
                        if (auth.getCurrentUser() != null){
                            auth.signOut();
                            startActivity(new Intent(MainActivity.this, Login.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }

                        break;
                    case R.id.mAccount:
                        if (account == null){
                            account = new Account();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.add(R.id.frame, account, "Account");
                            tags.add("Account");
                            fragmentClasses.add(new FragmentClass(account, "Account"));
                            transaction.commit();
                        }
                        else {
                            tags.remove("Account");
                            tags.add("Account");
                        }
                        setVisibility("Account");
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public void checkBottom(String tag){
        Menu menu = (Menu) bottomNavigationView.getMenu();
        MenuItem menuItem = null;
        if (tag.equals("Companies")){
            menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
        else if (tag.equals("Search")){
            menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        }
        else if (tag.equals("Cart")){
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
        }
        else if (tag.equals("Account")){
            menuItem = menu.getItem(3);
            menuItem.setChecked(true);
        }

    }

    public void checkNav(String tag){
        Menu menu = (Menu) navigationView.getMenu();
        MenuItem menuItem = null;
        if (tag.equals("Companies")){
            menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        }
        else if (tag.equals("Orders")){
            menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        }
        else if (tag.equals("Cart")){
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
        }
        else if (tag.equals("Account")){
            menuItem = menu.getItem(3);
            menuItem.setChecked(true);
        }
    }

    public void setVisibility(String tag){
        if (tag.equals("Companies")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("MEDISUPP");
        }
        else if (tag.equals("Orders")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("My Orders");
        }
        else if (tag.equals("Cart")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Cart");
        }
        else if (tag.equals("Account")){
            hideBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("My Profile");
        }
        else if (tag.equals("Categories")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("CAT");
        }
        else if (tag.equals("Details")){
            hideBottom();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tag.equals("Sterilization")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Sterilization");
        }
        else if (tag.equals("Maternity")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Maternity");

        }
        else if (tag.equals("Surgery")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Surgery");
        }
        else if (tag.equals("Laboratory")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Laboratory");
        }
        else if (tag.equals("NewArrivals")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("New Arrivals");
        }
        else if (tag.equals("Popular")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Popular");
        }
        else if (tag.equals("Favorites")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Favourites");
        }
        else if (tag.equals("Mpesa")){
            hideBottom();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tag.equals("Paypal")){
            hideBottom();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tag.equals("Search")){
            hideBottom();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tag.equals("POD")){
            hideBottom();
            coordinatorLayout.setVisibility(View.GONE);
        }
        else if (tag.equals("Notification")){
            showBottom();
            coordinatorLayout.setVisibility(View.VISIBLE);
            heading.setText("Notifications");

        }
        for (int i = 0; i < fragmentClasses.size(); i++){
            if (tag.equals(fragmentClasses.get(i).getTitle())){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.show(fragmentClasses.get(i).getFragment());
                transaction.commit();
            }
            else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(fragmentClasses.get(i).getFragment());
                fragmentTransaction.commit();
            }
            checkBottom(tag);
            checkNav(tag);
        }

    }
    public void showBottom(){
        if (bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
    public void hideBottom(){
        if (bottomNavigationView != null){
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        int total = tags.size();
        if (total > 1){
            String top = tags.get(total - 1);
            String bottom = tags.get(total - 2);
            setVisibility(bottom);
            tags.remove(top);
            mCount = 0;
        }
        else if (total == 1){
            String tp = tags.get(total - 1);
            if (tp.equals("Companies")){
                mCount++;
                finish();
                Toast.makeText(this, "end", Toast.LENGTH_SHORT).show();
            }
            else {
                mCount++;
                finish();
            }
        }
        else if (total >= 1){
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_item, menu);
        View n = menu.findItem(R.id.notification).getActionView();
        TextView txt = (TextView) n.findViewById(R.id.badge_tool);
        txt.setVisibility(View.GONE);
        reference = database.getReference().child("PendingOrders").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<Orders> notificationsClassesList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Orders orders = new Orders();
                        orders.setOrderId(snap.child("orderId").getValue().toString());
                        orders.setPrice(((Long)snap.child("price").getValue()).intValue());
                        orders.setNotification(snap.child("notification").getValue().toString());
                        notificationsClassesList.add(orders);
                    }
                    if (!notificationsClassesList.isEmpty()){
                        txt.setVisibility(View.VISIBLE);
                        txt.setText(String.valueOf(notificationsClassesList.size()));
                    }
                    else {
                        txt.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationsFragment == null){
                    notificationsFragment = new NotificationsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.add(R.id.frame, notificationsFragment, "Notification");
                    transaction.commit();
                    tags.add("Notification");
                    fragmentClasses.add(new FragmentClass(notificationsFragment, "Notification"));
                }
                else {
                    tags.remove("Notification");
                    tags.add("Notification");
                }
                setVisibility("Notification");
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.location:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.notification:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        if (auth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, Login.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        super.onStart();
    }

    @Override
    public void getCompany(Companies companies) {
        String title = companies.getName();
        //toolbar.setTitle(title);
        if (categoriesFragment == null){
            categoriesFragment = new CategoriesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame, categoriesFragment, "Categories");
            tags.add("Categories");
            fragmentClasses.add(new FragmentClass(categoriesFragment, "Categories"));
            transaction.commit();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else {
            tags.remove("Categories");
            tags.add("Categories");
        }
        setVisibility("Categories");
    }

    @Override
    public void getData(Products products) {
        //toolbar.setTitle(products.getName());
        if (detailsFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commitAllowingStateLoss();
        }
        detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", products);
        detailsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.add(R.id.frame, detailsFragment, "Details");
        tags.add("Details");
        fragmentClasses.add(new FragmentClass(detailsFragment, "Details"));
        transaction.commit();
        setVisibility("Details");
    }

    @Override
    public void getLabData() {
        if (laboratoryFragment == null){
            laboratoryFragment = new LaboratoryFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.add(R.id.frame, laboratoryFragment, "Laboratory");
            tags.add("Laboratory");
            fragmentClasses.add(new FragmentClass(laboratoryFragment, "Laboratory"));
            transaction.commit();
        }
        else {
            tags.remove("Laboratory");
            tags.add("Laboratory");
        }
        setVisibility("Laboratory");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void getSurgeryData() {
        if (surgeryFragment == null){
            surgeryFragment = new SurgeryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.frame, surgeryFragment, "Surgery");
            tags.add("Surgery");
            fragmentClasses.add(new FragmentClass(surgeryFragment, "Surgery"));
            fragmentTransaction.commit();
        }
        else {
            tags.remove("Surgery");
            tags.add("Surgery");
        }
        setVisibility("Surgery");
    }

    @Override
    public void getMaternityData() {
        if (maternityFragment == null){
            maternityFragment = new MaternityFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.add(R.id.frame, maternityFragment, "Maternity");
            tags.add("Maternity");
            fragmentClasses.add(new FragmentClass(maternityFragment, "Maternity"));
            transaction.commit();
        }
        else {
            tags.remove("Maternity");
            tags.add("Maternity");
        }
        setVisibility("Maternity");

    }

    @Override
    public void getSterilisationData() {
        if (sterilizationFragment == null){
            sterilizationFragment = new SterilizationFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.add(R.id.frame, sterilizationFragment, "Sterilization");
            tags.add("Sterilization");
            fragmentClasses.add(new FragmentClass(sterilizationFragment, "Sterilization"));
            transaction.commit();
        }
        else {
            tags.remove("Sterilization");
            tags.add("Sterilization");
        }
        setVisibility("Sterilization");

    }

    @Override
    public void addToCart(Products products) {
        String name = products.getName();
        String description = products.getDescription();
        String rate = products.getRating();
        int price = products.getPrice();
        String image = products.getImgUrl();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Cart").child(auth.getUid()).push();
        Products products1 = new Products(name, rate, price, image, description);
        reference.setValue(products1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference().child("Cart").child(auth.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Products> products2 = new ArrayList<Products>();
                        if (snapshot.exists()){
                            for (DataSnapshot snap : snapshot.getChildren()){
                                Products products = new Products();
                                products.setName(snap.child("name").getValue().toString());
                                products.setPrice(((Long)snap.child("price").getValue()).intValue());
                                products.setRating(snap.child("rating").getValue().toString());
                                products.setDescription(snap.child("description").getValue().toString());
                                products.setImgUrl(snap.child("imgUrl").getValue().toString());
                                products2.add(products);

                                if (!products2.isEmpty()){
                                    BottomNavigationItemView bottomNavigationItemView = bottomNavigationView.findViewById(R.id.cart);
                                    View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.not_layout, bottomNavigationView, false);
                                    TextView noT = v.findViewById(R.id.badge_text_view);
                                    noT.setText(String.valueOf(products2.size()));
                                    bottomNavigationItemView.addView(v);
                                }
                                else if (products2.isEmpty()){
                                    products2.clear();
                                    BottomNavigationItemView btView = bottomNavigationView.findViewById(R.id.cart);
                                    btView.removeViewAt(3);
                                }

                            }

                        }
                        else {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void deleteItem(Products products) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Cart").child(auth.getUid());
        Query query = reference.orderByChild("name").equalTo(products.getName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    snap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void getPopular() {
        if (popularFragment == null){
            popularFragment = new PopularFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.frame, popularFragment, "Popular");
            tags.add("Popular");
            fragmentClasses.add(new FragmentClass(popularFragment, "Popular"));
            fragmentTransaction.commit();
        }
        else {
            tags.remove("Popular");
            tags.add("Popular");
        }
        setVisibility("Popular");

    }

    @Override
    public void getNewArrivals() {
        if (newArrivalsFragment == null){
            newArrivalsFragment = new NewArrivalsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.frame, newArrivalsFragment, "NewArrivals");
            tags.add("NewArrivals");
            fragmentClasses.add(new FragmentClass(newArrivalsFragment, "NewArrivals"));
            fragmentTransaction.commit();
        }
        else {
            tags.remove("NewArrivals");
            tags.add("NewArrivals");
        }
        setVisibility("NewArrivals");
    }

    @Override
    public void getFavorites(Products products) {
        String name = products.getName();
        String description = products.getDescription();
        String img = products.getImgUrl();
        int price = products.getPrice();
        String rate = products.getRating();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Favourites").child(auth.getUid()).push();
        Products products1 = new Products(name, rate, price, img, description);
        reference.setValue(products1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void removeFav(Products products) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Favourites").child(auth.getUid());
        Query query = reference.orderByChild("name").equalTo(products.getName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    snap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void trackOrders() {
        if (ordersFragment == null){
            ordersFragment = new OrdersFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.add(R.id.frame, ordersFragment, "Orders");
            tags.add("Orders");
            fragmentClasses.add(new FragmentClass(ordersFragment, "Orders"));
            fragmentTransaction.commit();
        }
        else {
            tags.remove("Orders");
            tags.add("Orders");
        }
        setVisibility("Orders");
    }

    @Override
    public void orderMpesa(int price) {
        if (mpesaFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mpesaFragment).commitAllowingStateLoss();
        }
        mpesaFragment = new MpesaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PRICE", price);
        mpesaFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.frame, mpesaFragment, "Mpesa");
        tags.add("Mpesa");
        fragmentClasses.add(new FragmentClass(mpesaFragment, "Mpesa"));
        transaction.commit();
        setVisibility("Mpesa");
    }

    @Override
    public void orderPaypal(int price) {
        if (paypalFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(paypalFragment).commitAllowingStateLoss();
        }
        paypalFragment = new PaypalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PRICE", price);
        paypalFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.frame, paypalFragment, "Paypal");
        tags.add("Paypal");
        fragmentClasses.add(new FragmentClass(paypalFragment, "Paypal"));
        transaction.commit();
        setVisibility("Paypal");

    }
    @Override
    public void orderPOD(int price) {
        if (payOnDeliveryFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(payOnDeliveryFragment).commitAllowingStateLoss();
        }
        payOnDeliveryFragment = new PayOnDeliveryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PRICE", price);
        payOnDeliveryFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.frame, payOnDeliveryFragment, "POD");
        tags.add("POD");
        fragmentClasses.add(new FragmentClass(payOnDeliveryFragment, "POD"));
        transaction.commit();
        setVisibility("POD");

    }
}
