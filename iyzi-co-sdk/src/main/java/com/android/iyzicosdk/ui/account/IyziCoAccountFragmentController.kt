package com.android.iyzicosdk.ui.account

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.*
import com.android.iyzicosdk.data.model.response.*
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.*
import com.android.iyzicosdk.util.extensions.clearSpace
import com.android.iyzicosdk.util.extensions.convertAmount
import com.android.iyzicosdk.util.extensions.intToBoolean
import com.android.iyzicosdk.util.extensions.isSuccess


internal class IyziCoAccountFragmentController constructor(private var baseFragment: IyziCoAccountFragment) {
    companion object {
        fun newInstance(baseFragment: IyziCoAccountFragment) =
            IyziCoAccountFragmentController(baseFragment)
    }

    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()
    private var merchantSecretKey = ""
    private var merchantApiKey = ""
    private var gsmNumber = ""
    private var memberId = ""

    fun getBanks(uiCallback: UIResponseCallBack<IyziCoBanksResponse>) {
        baseFragment.showLoadingAnimation()
        iyziCoRepository.getBanks(object : IyziCoServiceCallback<IyziCoBanksResponse> {
            override fun onSuccess(data: IyziCoBanksResponse?) {
                data?.items?.let {
                    baseFragment.setBankAdapter(data.items, data.referenceCode)
                }
            }

            override fun onError(code: Int, message: String) {
                uiCallback.onError(code, message)
            }
        })

    }

    fun getInstallments(
        binNumber: String,
        locale: String,
        price: String,
        type: IyziCoInstallmentType,
        withInquire: Boolean = false
    ) {
        if (type == IyziCoInstallmentType.NORMAL) {
            baseFragment.showLoadingAnimation()
        }
        IyziCoResourcesConstans.IyziCOxTokenUse = true
        var myList: List<IyziCoInstallmentPrice>
        myList = emptyList();
        iyziCoRepository.pwiRetrieveInstallments(
            IyziCoRetriveInstalmentRequest(
                binNumber,
                locale,
                price
            ), object : IyziCoServiceCallback<IyziCoPwiRetrieveInstalmentsResponse> {
                override fun onSuccess(response: IyziCoPwiRetrieveInstalmentsResponse?) {
                    if (response?.status == "failure") {
                        baseFragment.hideLoadingAnimation()
                        baseFragment.showSDKError(response?.errorMessage ?: "")

                    } else {
                        val data = response?.iyziCoInstallmentDetails!!
                        baseFragment.setForce3Ds(response.iyziCoInstallmentDetails[0].force3ds.intToBoolean())
                        baseFragment.hideLoadingAnimation()
                        for (detail in data) {
                            myList = myList + detail.iyziCoInstallmentPrices
                        }
                        baseFragment.setInstallmentAdapter(myList)
                    }

                    if (withInquire) baseFragment.getInquireService()
                }

                override fun onError(code: Int, message: String) {
                    baseFragment.hideLoadingAnimation()
                    baseFragment.showSDKError(message)
                    if (withInquire) baseFragment.getInquireService()
                }

            })

    }

    private fun payWithThreeD(
        iyziCopayWithCardRequest: IyziCopayWithCardRequest
    ) {
        baseFragment.showLoadingAnimation()

        IyziCoResourcesConstans.IyziCOxTokenUse = true
        iyziCoRepository.payWithThreeD(
            iyziCopayWithCardRequest,
            object : IyziCoServiceCallback<IyziCoPWIResponse> {
                override fun onSuccess(response: IyziCoPWIResponse?) {
                    baseFragment.hideLoadingAnimation()
                    if (response?.status == "failure") {
                        baseFragment.navigate(
                            IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.ERROR),
                            false
                        )
                        baseFragment.showSDKError(response.errorMessage ?: "")

                    } else {
                        baseFragment.setWebView(response?.threeDSHtmlContent ?: "")

                    }
                }

                override fun onError(code: Int, message: String) {
                    baseFragment.hideLoadingAnimation()
                    baseFragment.navigate(
                        IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.ERROR),
                        false
                    )

                }
            })
    }

    fun bankTransferInitialize(
        bankId: Int,
        iban: String,
        title: String,
        bankName: String,
        bankLogo: String
    ) {
        baseFragment.showLoadingAnimation()
        IyziCoResourcesConstans.IyziCOxTokenUse = true
        iyziCoRepository.pwiBankTransferInitialize(
            IyziCoBankTransferInitiaalize(
                bankId,
                true,
                IyziCoResourcesConstans.IyziCoEmail,
                gsmNumber,
                IyziCoConfig.LANGUAGE.type,
                memberId.toInt(),
                IyziCoLoginChannelType.THIRD_PARTY_APP.type
            ), object : IyziCoServiceCallback<IyziCoPWIResponse> {
                override fun onSuccess(data: IyziCoPWIResponse?) {
                    baseFragment.hideLoadingAnimation()
                    if (data?.status == "failure") {
                        baseFragment.showSDKError(data.errorMessage ?: "")
                    } else {
                        baseFragment.showRemittanceInformationBottomSheetForPwi(
                            iban,
                            title,
                            bankName,
                            data?.code ?: "",
                            data?.bankTransferPaymentId ?: "",
                            bankLogo
                        )
                    }

                }

                override fun onError(code: Int, message: String) {
                    baseFragment.hideLoadingAnimation()
                    baseFragment.showSDKError(message ?: "")


                }
            })

    }

    private fun payhWithCard(pwiCardRequest: IyziCopayWithCardRequest) {
        baseFragment.showLoadingAnimation()

        IyziCoResourcesConstans.IyziCOxTokenUse = true
        iyziCoRepository.payWithCard(pwiCardRequest,
            object : IyziCoServiceCallback<IyziCoPWIResponse> {
                override fun onSuccess(data: IyziCoPWIResponse?) {
                    baseFragment.hideLoadingAnimation()
                    if (data?.status == "failure") {
                        baseFragment.showSDKError(data.errorMessage ?: "")
                    } else {

                        if (data != null && !data.threeDSHtmlContent.isNullOrEmpty()) {
                            baseFragment.setWebView(data.threeDSHtmlContent ?: "")
                        } else {
                            baseFragment.navigate(
                                IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.TRANSFER),
                                false
                            )
                        }
                    }
                }

                override fun onError(code: Int, message: String) {
                    baseFragment.showSDKError(message ?: "")
                    baseFragment.hideLoadingAnimation()
                    baseFragment.navigate(
                        IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.ERROR),
                        false
                    )
                }
            })
    }

    fun getMemberCards(uiCallback: UIResponseCallBack<IyziCoRetrieveMemberCardsResponse>) {
        baseFragment.showLoadingAnimation()
        iyziCoRepository.retriverMemberCards(object :
            IyziCoServiceCallback<IyziCoRetrieveMemberCardsResponse> {
            override fun onSuccess(data: IyziCoRetrieveMemberCardsResponse?) {
                uiCallback.onSuccess(data)
                data?.iyziCoCardItems?.let {
                    baseFragment.setCardAdapter(it)
                    if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.TOPUP) {
                        baseFragment.myCardsAutoFocus()
                        baseFragment.setFirstCard()
                    }
                }
                baseFragment.getRetriverBalance()
            }

            override fun onError(code: Int, message: String) {
                baseFragment.hideLoadingAnimation()
                baseFragment.myCardsAutoFocus()
            }
        })
    }

    fun getRetrieverMemberBalance(
        uiCallBack: UIResponseCallBack<String>,
        isFirstBalanceRequest: Boolean
    ) {
        clearMerchantKeys()
        iyziCoRepository.retrieverMemberBalance(object :
            IyziCoServiceCallback<IyziCoRetrieverMemberBalanceResponse> {
            override fun onSuccess(data: IyziCoRetrieverMemberBalanceResponse?) {
                setMerchantKeys()
                data?.let { it ->
                    it.amount.let {
                        uiCallBack.onSuccess(it)
                    }
                    if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                        baseFragment.setBalance()
                        if (isFirstBalanceRequest) {
                            baseFragment.checkMyAccount()
                        }
                    } else {
                        baseFragment.showIyziCoBalance(it.amount.convertAmount())
                    }
                    if (!isFirstBalanceRequest) {
                        baseFragment.setBalanceToRefresh()
                    }
                }
            }

            override fun onError(code: Int, message: String) {
                setMerchantKeys()
                baseFragment.setZeroBalance()
                baseFragment.setBalance()
                if (isFirstBalanceRequest) {
                    baseFragment.checkMyAccount()
                }

            }
        })
    }

    fun createDepositRegisteredCard(
        initialRequestId: String,
        cardToken: String,
        amount: String,
        currencyCode: String,
        clientIp: String,
        channelType: String,
        uiCallback: UIResponseCallBack<IyziCoCreateDepositCardResponse>
    ) {
        baseFragment.showLoadingAnimation()
        iyziCoRepository.createDepositRegisteredCard(
            IyziCoCreateDepositRegisteredCardRequest(
                amount,
                cardToken,
                channelType,
                clientIp,
                currencyCode,
                initialRequestId
            ), object : IyziCoServiceCallback<IyziCoCreateDepositCardResponse> {
                override fun onSuccess(data: IyziCoCreateDepositCardResponse?) {
                    data?.let {
                        uiCallback.onSuccess(it)
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                }
            })
    }

    fun createDepositWithNewCard(
        amount: String,
        cardHolderName: String,
        cardNumber: String,
        channelType: String,
        clientIp: String,
        currencyCode: String,
        cvv: String,
        expireMonth: String,
        expireYear: String,
        initialRequestId: String,
        uiCallback: UIResponseCallBack<IyziCoCreateDepositCardResponse>
    ) {
        baseFragment.showLoadingAnimation()
        iyziCoRepository.createDepositNewCard(
            IyziCoCreateDepositNewCardRequest(
                amount,
                cardHolderName,
                cardNumber,
                channelType,
                clientIp,
                currencyCode, cvv, expireMonth, expireYear, initialRequestId
            ), object : IyziCoServiceCallback<IyziCoCreateDepositCardResponse> {
                override fun onSuccess(data: IyziCoCreateDepositCardResponse?) {
                    uiCallback.onSuccess(data)
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                }

            })
    }


    fun pwiRetrive(
        token: String,
        locale: String,
        uiCallback: UIResponseCallBack<IyziCoPWIRetriveResponse>
    ) {
        baseFragment.showLoadingAnimation()

        iyziCoRepository.pwiRetriveService(IyziCoPWIRetriveRequest(token, locale),
            object : IyziCoServiceCallback<IyziCoPWIRetriveResponse> {
                override fun onSuccess(data: IyziCoPWIRetriveResponse?) {
                    data?.let {
                        uiCallback.onSuccess(it)
                        gsmNumber = "+90" + IyziCoResourcesConstans.IyziPhoneNumber
                        gsmNumber.clearSpace()
                        memberId = data.memberId

                        val tempCards = it.iyziCoMemberCards.map {
                            it.toCardItem()
                        }


                        val cards = tempCards.sortedByDescending { it.isIyziCoCard.toString() }
                            .sortedByDescending { it.isIyzicoVirtualCard.toString() }

                        baseFragment.setCardAdapter(cards)


                        baseFragment.setVisibleEFTTab(false)


                        baseFragment.setForce3Ds(data.iyziCoCheckoutDetail.force3Ds)
                        baseFragment.getRetriverBalance()
                        baseFragment.setBankAdapter(
                            data.iyziCoCheckoutDetail.bankTransferAccounts,
                            "123456789"
                        )


                        val plusInstallmentList =
                            it.iyziCoCheckoutDetail.plusInstallmentResponseList?.sortedByDescending { it.startDate }
                        baseFragment.setPlusInstallment(plusInstallmentList ?: emptyList())
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                }
            })
    }

    //iyziCo hesabındaki para ile ödeme yapılırken kullanılacak servis
    private fun pwiPayWithBalance(
        type: IyziCoLoginChannelType
    ) {
        baseFragment.showLoadingAnimation()
        IyziCoResourcesConstans.IyziCOxTokenUse = true
        iyziCoRepository.pwiPayWithBalance(IyziCoPayWithBalanceRequest(type.type),
            object : IyziCoServiceCallback<IyziCoPayWithBalanceResponse> {
                override fun onSuccess(data: IyziCoPayWithBalanceResponse?) {
                    data?.let {
                        baseFragment.hideLoadingAnimation()
                        baseFragment.navigate(
                            IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.TRANSFER),
                            false
                        )
                    }
                }

                override fun onError(code: Int, message: String) {
                    baseFragment.hideLoadingAnimation()
                    baseFragment.navigate(
                        IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.ERROR),
                        false
                    )
                }

            })
    }

    //card token kayıtlı kart ile ödeneceği zaman kullanılır ve diğer alanlar boş bırakılır.
    //yeni kart ile ödeme yapılacaksa card token boş bırakılır ve diğer alanlar kullanılır.
    private fun payWithMixpayment(
        memberBalanceAmount: Double,
        paymentChannelType: IyziCoLoginChannelType,
        cardToken: String? = null,
        cardHolderName: String? = null,
        cardNumber: String? = null,
        cvc: String? = null,
        expireMonth: String? = null,
        expireYear: String? = null,
        registerCard: Int? = null,
        registerConsumer: Boolean? = null,
        memberToken: String? = null,
        rewardRequest: RewardRequest? = null
    ) {

        baseFragment.showLoadingAnimation()
        IyziCoResourcesConstans.IyziCOxTokenUse = true
        iyziCoRepository.payWithMixpayment(
            IyziCoPwiMixPaymentRequest(
                memberBalanceAmount,
                IyziCoPaymentCard(
                    cardToken,
                    cardHolderName,
                    cardNumber,
                    cvc,
                    expireMonth,
                    expireYear,
                    registerCard,
                    registerConsumer,
                    if (cardToken.isNullOrEmpty()) null else IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY
                ), paymentChannelType.type, memberToken, rewardRequest
            ), object : IyziCoServiceCallback<IyziCoPWIResponse> {
                override fun onSuccess(data: IyziCoPWIResponse?) {
                    data?.let {
                        baseFragment.hideLoadingAnimation()
                        if (data.status == "failure") {
                            baseFragment.showSDKError(data.errorMessage ?: "")
                        } else {
                            baseFragment.navigate(
                                IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.TRANSFER),
                                false
                            )
                        }
                    }
                }

                override fun onError(code: Int, message: String) {
                    baseFragment.hideLoadingAnimation()
                    baseFragment.navigate(
                        IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.ERROR),
                        false
                    )
                }
            }
        )
    }

    fun initUi() {
        baseFragment.hideNewCardContainer()
        baseFragment.showCloseButton()
        baseFragment.unfocusListener()
        baseFragment.cardNumberTextWatcher()
        baseFragment.setNameEdittext()

        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            baseFragment.pwiRetrive()
        } else {
            baseFragment.getCards()

        }
    }

    private fun clearMerchantKeys() {
        merchantApiKey = IyziCoConfig.MERCHANT_API_KEY
        merchantSecretKey = IyziCoConfig.MERCHANT_SECRET_KEY
        IyziCoConfig.MERCHANT_API_KEY = ""
        IyziCoConfig.MERCHANT_SECRET_KEY = ""
    }

    private fun setMerchantKeys() {
        IyziCoConfig.MERCHANT_API_KEY = merchantApiKey
        IyziCoConfig.MERCHANT_SECRET_KEY = merchantSecretKey
    }

    fun setPayment(
        iyziCoPaymentType: IyziCoPaymentType,
        force3Ds: Boolean,
        balance: Double,
        loginChannelType: IyziCoLoginChannelType,
        cardToken: String,
        cardName: String,
        cardNumber: String,
        cardCvc: String,
        mounth: String,
        year: String,
        registerCard: Int,
        registerConsumer: Boolean,
        buyerProtected: Boolean,
        installment: Int,
        memberToken: String,
        paidPrice: String,
        isUsedbalance: Boolean,
        rewardPoint: Double
    ) {

        val rewardRequest = if (rewardPoint > 0.0) RewardRequest(rewardPoint, 1) else null

        if (iyziCoPaymentType == IyziCoPaymentType.BALANCE) {
            pwiPayWithBalance(loginChannelType)

        } else if (iyziCoPaymentType == IyziCoPaymentType.NEW_CARD_WITH_BALANCE) {
            payWithMixpayment(
                balance,
                loginChannelType,
                null,
                cardName,
                cardNumber,
                cardCvc,
                mounth,
                year,
                registerCard,
                registerConsumer,
                rewardRequest = rewardRequest
            )
        } else if (iyziCoPaymentType == IyziCoPaymentType.CARD_WITH_BALANCE) {
            payWithMixpayment(
                balance,
                loginChannelType,
                memberToken = memberToken,
                cardToken = cardToken,
                rewardRequest = rewardRequest
            )
        } else if (!force3Ds) {
            when (iyziCoPaymentType) {
                IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE -> {
                    payhWithCard(
                        IyziCopayWithCardRequest(
                            true, gsmNumber, installment, memberId, memberToken, paidPrice,
                            IyziCoPaymentCard(
                                null,
                                cardName,
                                cardNumber,
                                cardCvc,
                                mounth,
                                year,
                                registerCard,
                                registerConsumer
                            ), loginChannelType.type,
                            rewardRequest
                        )
                    )
                }
                IyziCoPaymentType.CARD_WITHOUT_BALANCE -> {
                    payhWithCard(
                        IyziCopayWithCardRequest(
                            true, gsmNumber, installment, memberId, memberToken, paidPrice,
                            IyziCoPaymentCard(
                                cardToken = cardToken,
                                consumerToken = IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY
                            ), loginChannelType.type,
                            rewardRequest
                        )
                    )
                }
            }

        } else {
            when (iyziCoPaymentType) {
                IyziCoPaymentType.NEW_CARD_WITHOUT_BALANCE -> {

                    payWithThreeD(
                        IyziCopayWithCardRequest(
                            buyerProtected,
                            gsmNumber,
                            installment,
                            memberId,
                            memberToken,
                            paidPrice,
                            IyziCoPaymentCard(
                                null,
                                cardName,
                                cardNumber,
                                cardCvc,
                                mounth,
                                year,
                                registerCard,
                                registerConsumer
                            ),
                            loginChannelType.type,
                            rewardRequest
                        )
                    )
                }
                IyziCoPaymentType.CARD_WITHOUT_BALANCE -> {
                    payWithThreeD(
                        IyziCopayWithCardRequest(
                            true, gsmNumber, installment, memberId, memberToken, paidPrice,
                            IyziCoPaymentCard(
                                cardToken = cardToken,
                                consumerToken = IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY
                            ), loginChannelType.type,
                            rewardRequest
                        )
                    )
                }
            }
        }
    }

    fun pwiInquire(
        conversationId: String,
        currency: String,
        locale: String,
        paidPrice: Double,
        paymentCardToken: String,
        paymentChannel: String,
        uiCallback: UIResponseCallBack<IyziCoInquireResponse>
    ) {
        IyziCoResourcesConstans.IyziCOxTokenUse = true
        baseFragment.showLoadingAnimation()

        val request = IyziCoInquireRequest(
            conversationId, currency, locale, paidPrice,
            IyziCoCardToken(paymentCardToken), paymentChannel
        )

        iyziCoRepository.pwiInquire(request, object : IyziCoServiceCallback<IyziCoInquireResponse> {
            override fun onSuccess(data: IyziCoInquireResponse?) {
                baseFragment.hideLoadingAnimation()
                if (data?.status.isSuccess()) {
                    uiCallback.onSuccess(data)
                } else {
                    uiCallback.onError(101, "")
                }
            }

            override fun onError(code: Int, message: String) {
                baseFragment.hideLoadingAnimation()
                uiCallback.onError(code, message)
            }
        })
    }

    fun pwiInquireWithNewCard(
        conversationId: String,
        currencyCode: String,
        locale: String,
        paidPrice: Double,
        cardHolderName: String,
        cardNumber: String,
        cardCvv: String,
        cardMonth: String,
        cardYear: String,
        uiCallback: UIResponseCallBack<IyziCoInquireResponse>
    ) {

        IyziCoResourcesConstans.IyziCOxTokenUse = true
        baseFragment.showLoadingAnimation()
        val request = IyziCoNewCardInquireRequest(
            conversationId, currencyCode, locale, paidPrice,
            IyziCoInquirePaymentCard(
                cardHolderName, cardNumber, cardCvv, cardMonth, cardYear
            ), IyziCoLoginChannelType.THIRD_PARTY_APP.type
        )

        iyziCoRepository.pwiInquireWithNewCard(
            request,
            object : IyziCoServiceCallback<IyziCoInquireResponse> {
                override fun onSuccess(data: IyziCoInquireResponse?) {
                    baseFragment.hideLoadingAnimation()
                    if (data?.status.isSuccess()) {
                        uiCallback.onSuccess(data)
                    } else {
                        uiCallback.onError(101, "")
                    }
                }

                override fun onError(code: Int, message: String) {
                    baseFragment.hideLoadingAnimation()
                    uiCallback.onError(code, message)
                }
            })
    }

    fun IyziCoMemberCard.toCardItem() = IyziCoCardItem(
        cardBankName = this.cardBankName ?: "",
        cardToken = this.cardToken ?: "",
        cardType = this.cardType ?: "",
        lastFourDigits = this.lastFourDigits ?: "",
        isSelected = false,
        cardAssociationLogoUrl = this.cardAssociationLogoUrl ?: "",
        cardAssociation = this.cardAssociation ?: "",
        binNumber = this.binNumber ?: "",
        isIyziCoCard = this.iyzicoCard, /*this.cardBankName.equals(IyziCoConfig.IYZICO, ignoreCase = false)*/
        isIyzicoVirtualCard = this.iyzicoVirtualCard,
        threeDSVerified = this.threeDSVerified
    )


}