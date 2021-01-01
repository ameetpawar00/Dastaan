package com.itsupportwale.dastaan.fragment

import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import io.reactivex.disposables.Disposable

interface FragmentBaseListener {
    fun onFragmentApiSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?)
    fun onFragmentApiFailure(message: String?, apiName: String?)
    fun onReadWriteStoragePermissionAllow(medialTypes: String?)
    fun onReadWriteStoragePermissionDeny(medialTypes: String?)
}
