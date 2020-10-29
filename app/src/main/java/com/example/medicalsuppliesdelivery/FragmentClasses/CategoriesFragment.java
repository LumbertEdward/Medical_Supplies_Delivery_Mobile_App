package com.example.medicalsuppliesdelivery.FragmentClasses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.medicalsuppliesdelivery.Adapters.viewPagerAdapter;
import com.example.medicalsuppliesdelivery.R;
import com.google.android.material.tabs.TabLayout;


public class CategoriesFragment extends Fragment {
    private ViewFlipper flipper;
    private int images[] = {R.drawable.med1, R.drawable.med7, R.drawable.med8, R.drawable.med8};
    private Animation fadeIn;
    private Animation fadeOut;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private viewPagerAdapter adapter;
    //fragments
    private LaboratoryFragment laboratoryFragment;
    private MaternityFragment maternityFragment;
    private SterilizationFragment sterilizationFragment;
    private SurgeryFragment surgeryFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        flipper = (ViewFlipper) v.findViewById(R.id.flipper);
        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_anim);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_anim);
        for (int i=0; i<images.length; i++){
            setFlipperImage(images[i]);
        }
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tab);
        laboratoryFragment = new LaboratoryFragment();
        maternityFragment = new MaternityFragment();
        sterilizationFragment = new SterilizationFragment();
        surgeryFragment = new SurgeryFragment();
        adapter = new viewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.getFragments(laboratoryFragment, "Laboratory");
        adapter.getFragments(maternityFragment, "Maternity");
        adapter.getFragments(surgeryFragment, "Surgery");
        adapter.getFragments(sterilizationFragment, "Sterilisation");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setFlipperImage(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        flipper.addView(imageView);
        flipper.setAutoStart(true);
        flipper.setFlipInterval(3000);
        flipper.setInAnimation(fadeIn);
        flipper.setOutAnimation(fadeOut);
    }
}
