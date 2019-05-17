package com.example.myapplicationisbetter.ui.userpage;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;

import io.reactivex.Completable;
import io.reactivex.functions.Consumer;

import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.userpage.MainView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public final int TEST_USER_PROFILE_DATA = 0;

    public void userListInit(){

        App.getInstance().getDatabase().userDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserDataModel>>() {
                    @Override
                    public void accept(List<UserDataModel> employees) throws Exception {
                        getViewState().setUserList(employees);
                    }
                });
    }
    public void setTextInFragment(UserDataModel userDataModel){
        if(userDataModel.future_id != TEST_USER_PROFILE_DATA){
        App.getInstance()
                .getDatabase()
                .userPropertiesDao()
                .getItemOnId(userDataModel.future_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<UserProperties>>() {
                    @Override
                    public void onSuccess(List<UserProperties> userProperties) {
                        getViewState().setTextInFragment(userDataModel,userProperties.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // ...
                    }
                });
        }else{
            getViewState().setTextInFragment(userDataModel,null);
        }
    }

    public void deleteAllInDB(){
        Completable.fromAction(()->{
               App.getInstance().getDatabase().userDao().deleteAll();
       }).subscribeOn(Schedulers.single()).subscribe();
        getViewState().deleteAllUsers();

    }


//public void initDataInBD(){
//        List<UserDataModel>  uList = new ArrayList<>();
//    Completable.fromAction(()->{
//        for (Integer i = 0;i<2;i++){
//            UserDataModel userDataModel = new UserDataModel(0,i.toString(),i.toString(),i.toString(),"",true,"","", R.drawable.photo,0);
//            uList.add(userDataModel);
//        }
//
//        App.getInstance().getDatabase().userDao().insertAll(uList);
//       }).subscribeOn(Schedulers.single()).subscribe();
//
//}



    public void onGo2() {

//       Completable.fromAction(()->{
//           UserDataModel userDataModel = new UserDataModel(
//                   userDataModel.birthday ="11/12/2342",
//           userDataModel.features="123456789",
//           userDataModel.firstName="ghjghj",
//           userDataModel.lastName = "ghjghj",
//           );
//
//               App.getInstance().getDatabase().userDao().insertAll(userDataModel);
//       }).subscribeOn(Schedulers.single()).subscribe();

    }

    public void onGo() {

//       App.getInstance().getDatabase().userDao().findByNameAsin("ghjghj")
//               .subscribeOn(Schedulers.io())
//               .observeOn(AndroidSchedulers.mainThread())
//               .subscribe(new DisposableMaybeObserver<UserDataModel>() {
//                   @Override
//                   public void onSuccess(UserDataModel userDataModel1) {
//                       getViewState().setStatus(userDataModel1.features);
//                   }
//
//                   @Override
//                   public void onError(Throwable e) {
//                       // ...
//                   }
//
//                   @Override
//                   public void onComplete() {
//                       // ...
//                   }
//               });
    }

}
