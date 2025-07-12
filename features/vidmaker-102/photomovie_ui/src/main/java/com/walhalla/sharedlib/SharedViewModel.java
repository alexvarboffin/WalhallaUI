//package com.walhalla.sharedlib;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//public class SharedViewModel extends ViewModel {
//
//    private final MutableLiveData<SharedObj> selectedItem = new MutableLiveData<>();
//
//    public void selectItem(SharedObj item) {
//        selectedItem.setValue(item);
//    }
//
//    public LiveData<SharedObj> getSelectedItem() {
//        return selectedItem;
//    }
//}
