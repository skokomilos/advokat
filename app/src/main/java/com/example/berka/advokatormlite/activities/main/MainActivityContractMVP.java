package com.example.berka.advokatormlite.activities.main;

import android.widget.Spinner;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.BaseFragment;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 19-Feb-18.
 */

public interface MainActivityContractMVP {

        interface View{

           void setListSlucajevi(List<Slucaj> slucajevi);

           void clickAddCard();
           void clickAllCard();
           void clickFindCard();

           void caseNotFound();

           void gotoFoundCaseFromFindCaseDialog(Slucaj slucaj);

           void gotoFoundCaseFromAllCasesDialog(Slucaj slucaj);

           String getCaseId();
           void showNoValuesTypedError();
           void enterNumberOfClients();

           void showAddCaseDialog(List<Postupak> postupci);
           void showAllCasesDialog(List<Slucaj> slucajevi, List<Double> izracunateTarife);


           //ovo bas i nije bitno, mozda neki metod koji je univerzalan za greske
           void displayNoPostupci();

           void startIntentKrivica(Postupak postupak, int broj_stranaka);

           void startIntentPrekrsaj(int postupakId, int broj_stranaka);

           void startIntentIsprave(int postupakId, int broj_stranaka);

           void startIntentOstaleKojeImajuProcenjivoNeprocenjivo(Postupak postupak, int broj_stranaka);


           void showCaseSavedMessage();
           void pleaseEnterNumber();
           void emptyEditTextWarning();
           void showOnlyNumbers();
           void gotoSpecificCaseActivity(Class activity, int postupakID, String brojStranaka);

            void updateLawyerName(String userName);

            void updateLawyerAddress(String address);

            void updateLawyerPlace(String place);
        }
        interface Presenter{

            void setView(MainActivityContractMVP.View view);

            void caseItemClicked(Slucaj slucaj);

            void findCaseButtonClicked(String slucajId);

            void addCaseButtonClicked(Postupak postupak, String broj_stranaka);

            void getAllPostupci();

            void getAllCases();

            List<Double> calculateCurrentValuesForAllCasses(List<Slucaj> slucajevi);

            void prefUserDialogSaveData(String name, String address, String place);

            void detachView();

            void prefUserDialogCreated();


        }

        interface Model{

            Slucaj getSlucaj(String caseId);

            List<Postupak> getPostupci();

            List<Slucaj> getSlucajevi();

            void updateUserInfo(String userName, String address, String place);

            String getCurentUserName();

            String getCurrentAddress();

            String getCurretnPlace();


        }
}
