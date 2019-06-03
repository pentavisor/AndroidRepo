package com.example.myapplicationisbetter.ui.userpage.CastomSpinner;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.ui.MyHelper;

public class CategoryDropdownMenu extends PopupWindow {

    private Context context;
    private RecyclerView rvCategory;
    private CategoryDropdownAdapter dropdownAdapter;
    private CloseWindowHandler closeWindowHandler;

    public CategoryDropdownMenu(Context context){
        super(context);
        this.context = context;
        setupView();
    }

    public void setCategorySelectedListener(CategoryDropdownAdapter.CategorySelectedListener categorySelectedListener) {
        dropdownAdapter.setCategorySelectedListener(categorySelectedListener);
    }
    public void setWindowClosedListener(CloseWindowHandler closeWindowHandler) {
        this.closeWindowHandler = closeWindowHandler;
    }

    private void setupView() {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_system_layout, null);

        rvCategory = view.findViewById(R.id.rvCategory);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvCategory.setBackgroundResource(R.drawable.circle_form_angle);
        rvCategory.addItemDecoration(new CastomDividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        dropdownAdapter = new CategoryDropdownAdapter(Category.generateCategoryList());
        rvCategory.setAdapter(dropdownAdapter);
        setBackgroundDrawable(null);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(view);
    }
    @Override
    public void dismiss(){
        super.dismiss();
        closeWindowHandler.onWindowClosed();
    }

    public interface CloseWindowHandler{
        void onWindowClosed();

    }

}
