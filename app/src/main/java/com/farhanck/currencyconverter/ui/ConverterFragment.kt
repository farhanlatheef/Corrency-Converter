package com.farhanck.currencyconverter.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.work.*
import com.farhanck.currencyconverter.R
import com.farhanck.currencyconverter.core.UIEventObserver
import com.farhanck.currencyconverter.data.db.Currency
import com.farhanck.currencyconverter.data.server.ExchangeRateUpdater
import com.farhanck.currencyconverter.databinding.FragmentConverterBinding
import com.farhanck.currencyconverter.core.binding.BindingFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.concurrent.TimeUnit


class ConverterFragment : BindingFragment<FragmentConverterBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_converter

    fun setUpdater() {
        val constraint  = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequest
            .Builder(ExchangeRateUpdater::class.java, 30, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()

        val workManager = WorkManager.getInstance(requireContext());
        workManager.enqueueUniquePeriodicWork("rate_updater", ExistingPeriodicWorkPolicy.KEEP, workRequest)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        val vm = getViewModel() as ConverterViewModel;

        binding.vm = vm
        binding.setLifecycleOwner(this);

        val observer  =  object : Observer<List<Currency>> {
            override fun onChanged(currencies: List<Currency>) {
                if(currencies.isNotEmpty()) {
                    vm.currencies.removeObserver(this);
                    setUpdater()
                };
            }
        }

        vm.currencies.observe(requireActivity(), observer);


        vm.errorToShow.observe(requireActivity(), UIEventObserver { it ->
            AppDialogs.showAlert(requireContext(), it, "close", object : AppDialogs.Positive{
                override fun onPositive() {
                    requireActivity().finish();
                }
            })
        })
    }


}

@BindingAdapter(value = ["currencies", "source"])
fun setCurrencies(spinner:AppCompatSpinner, currencies : List<Currency>?,  source:String) {
    val currencies = currencies ?: return;
    if(currencies.size == 0) return;
    spinner.adapter = ArrayAdapter(spinner.context, R.layout.spinner_item, currencies);
    spinner.setSelection(Currency.getListIndex(source, currencies))
}

