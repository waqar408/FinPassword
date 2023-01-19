package com.zb.finpassword.ui.privacy

import android.os.Bundle
import android.util.Log
import android.view.View
import com.nosugar.pref.AppConstant
import com.nosugar.ui.base.BaseFragment
import com.zb.deepstudy.enums.Status
import com.zb.finpassword.R
import com.zb.finpassword.ui.privacy.ViewModel.HomeViewModel
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.dialog_common.*
import kotlinx.android.synthetic.main.fragment_privacy.*
import org.json.JSONException
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class PrivacyFragment : BaseFragment(R.layout.fragment_privacy) {
    private val homeViewModel: HomeViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
    }

    fun setUi() {
        progressDialog = MyCustomProgressDialog(requireActivity())

        callKeyApi()
    }

    private fun callKeyApi() {

        val params = mapOf(
            "userid" to Prefs.getValue(requireActivity(), AppConstant.USER_ID, "").toString(),
            "pwd" to Prefs.getValue(requireActivity(), AppConstant.PASSWORD, "").toString()

        )
        homeViewModel.getKey(params)
        setUpObserver()

    }

    fun setUpObserver() {

        homeViewModel.homeResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    requireActivity().hideKeyboard()
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    var myobj = it.data?.privat


                    try {
                        val privacy = myobj.toString().replace("(", "")
                            .replace(")", "")
                            .replace("Privat", "")

                        val privacyList: List<String> = privacy.split(",")
                        var privacyValueList: ArrayList<String> = ArrayList<String>()
                        var privacyKeyList: ArrayList<String> = ArrayList<String>()

                        for (i in 0 until privacyList.size) {

                            /*      val wordToFind = "="
                           val word: Pattern = Pattern.compile(wordToFind)
                           val match: Matcher = word.matcher(privacyList.get(i))

                           while (match.find()) {

                               val result: String = privacyList.get(i).substring(match.start())
*/
                            val result: String = privacyList.get(i)
                                .substring(privacyList.get(i).lastIndexOf('=') + 1)

                            privacyValueList.add(result.replace("=", ""))

                            val resultKey = privacyList.get(i).substringBeforeLast("=")

                            privacyKeyList.add(resultKey.replace("=", ""))


                        }
                        Log.e("privacy", privacyKeyList.toString())

                        rv_home.apply {
                            adapter = HomeAdapter(
                                requireActivity(),
                                privacyValueList,
                                privacyKeyList,
                                "private"
                            )
                        }

                    } catch (e: JSONException) {
                        // YOU MAY WANT TO MAKE A TOAST HERE AS WELL
                        e.printStackTrace()
                    }

                }


                Status.ERROR -> {
                    progressDialog!!.dismiss()
                    showToasty(requireActivity(), it.message!!, "2")

                }

                Status.NETWORK_ERROR -> {
                    progressDialog!!.dismiss()
                    it.message?.let { message ->
                        requireActivity().showDialogN(R.layout.dialog_common) {
                            messageLTV?.text = String.format(message)
                            submitLBTN?.text = requireActivity().getStr(R.string.retry)
                            submitLBTN?.setSafeOnClickListener {
                                dismissLoader()
                                callCategoryApi()
                            }
                        }
                    }

                }
            }
        })
        homeViewModel.keyResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    Prefs.setValue(
                        requireActivity(),
                        AppConstant.API_KEY,
                        it.data?.key.toString()
                    )
                    callCategoryApi()

                    Log.e("key", it.data?.key.toString())
                }


                Status.ERROR -> {
                    progressDialog!!.dismiss()
                    showToasty(requireActivity(), it.message!!, "2")

                }

                Status.NETWORK_ERROR -> {
                    progressDialog!!.dismiss()
                    it.message?.let { message ->
                        requireActivity().showDialogN(R.layout.dialog_common) {
                            messageLTV?.text = String.format(message)
                            submitLBTN?.text = requireActivity().getStr(R.string.retry)
                            submitLBTN?.setSafeOnClickListener {
                                dismissLoader()
                                callKeyApi()
                            }
                        }
                    }

                }
            }
        })


    }

    private fun callCategoryApi() {

        val params = mapOf(
            "userid" to Prefs.getValue(requireActivity(), AppConstant.USER_ID, "").toString(),
            "pwd" to Prefs.getValue(requireActivity(), AppConstant.PASSWORD, "").toString(),
            "key" to Prefs.getValue(requireActivity(), AppConstant.API_KEY, "").toString()

        )
        homeViewModel.getCategory(params)
        setUpObserver()

    }


}