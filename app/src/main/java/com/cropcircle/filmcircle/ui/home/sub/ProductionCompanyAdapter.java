package com.cropcircle.filmcircle.ui.home.sub;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ItemProductionCompanyBinding;
import com.cropcircle.filmcircle.models.ProductionCompany;

import org.jetbrains.annotations.NotNull;

public class ProductionCompanyAdapter extends BaseQuickAdapter<ProductionCompany, BaseDataBindingHolder> {
    int layoutId;
    public ProductionCompanyAdapter(int layoutResId) {
        super(layoutResId);

        this.layoutId = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder baseDataBindingHolder, ProductionCompany productionCompany) {
        if (layoutId == R.layout.item_production_company){
            ItemProductionCompanyBinding binding = (ItemProductionCompanyBinding) baseDataBindingHolder.getDataBinding();
            binding.setData(productionCompany);
            binding.executePendingBindings();
        }
    }
}
