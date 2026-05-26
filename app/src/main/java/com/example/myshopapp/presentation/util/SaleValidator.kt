package com.example.myshopapp.presentation.util

import com.example.myshopapp.data.remote.model.request.sale.VatAmount
import com.example.myshopapp.presentation.state.CartItem

object SaleValidator {

    fun validateCashier(name: String): String? {
        if (name.isBlank()) return "Kassir adı boş ola bilməz"

        if (name.length > 64) return "Kassir adı maksimum 64 simvol ola bilər"


        return null
    }

    fun validateCurrency(currency: String): String? {
        if (currency != "AZN") return "Valyuta yalnız AZN ola bilər"

        return null
    }

    fun validateItemName(name: String): String? {
        if (name.isBlank()) return "Məhsul adı boş ola bilməz"


        if (name.length > 255) return "Məhsul adı maksimum 255 simvol ola bilər: '$name'"
        return null
    }

    fun validateItemCode(code: String): String? {
        if (code.isBlank()) return "Məhsul kodu boş ola bilməz"

        if (code.length > 32) return "Məhsul kodu maksimum 32 simvol ola bilər: '$code'"


        return null
    }

    fun validateItemCodeType(codeType: Int, code: String): String? {
        if (codeType !in listOf(0, 1, 2, 3)) return "itemCodeType yalnız 0, 1, 2, 3 ola bilər"
        when (codeType) {
            1 -> if (!code.all { it.isDigit() } || code.length != 8)
                return "EAN8 barkodu tam olaraq 8 rəqəm olmalıdır: '$code'"

            2 -> if (!code.all { it.isDigit() } || code.length != 13)
                return "EAN13 barkodu tam olaraq 13 rəqəm olmalıdır: '$code'"
        }
        return null
    }


    fun validateItemQuantity(qty: Float): String? {
        if (qty < 0.001f) return "itemQuantity minimum 0.001 olmalıdır"

        if (qty > 99.999f) return "itemQuantity maksimum    99.999 ola bilər"


        val rounded = Math.round(qty * 1000).toFloat() / 1000f

        if (Math.abs(rounded - qty) > 0.0001f) return "itemQuantity maksimum 3 onluq dəqiqlik ola bilər"
        return null
    }

    fun validateItemPriceAndSum(price: Double, qty: Float, sum: Double): String? {

        if (price < 0) return "itemPrice mənfi ola bilməz"

        if (sum < 0) return "itemSum mənfi ola bilməz"

        val expected = "%.2f".format(price * qty).toDouble()

        if (Math.abs(expected - sum) > 0.01) return "itemSum ($sum) itemPrice×itemQuantity ($expected) ilə uyğun gəlmir"
        return null
    }

    fun validateItemVatPercent(vat: Double): String? {
        if (vat !in listOf(0.0, 2.0, 8.0, 18.0))

            return "itemVatPercent yalnız 0, 2, 8 və ya 18 ola bilər (cari dəyər: $vat)"
        return null
    }

    fun validateAgroItem(
        isAgro: Boolean,
        marginPrice: Double?,
        marginSum: Double?,
        itemSum: Double,
        qty: Float,
        creditSum: Double,
        prepaymentSum: Double,
    ): String? {
        if (!isAgro) return null
        if (marginPrice == null || marginPrice <= 0) return "Agro məhsul üçün itemMarginPrice mütləqdir və 0-dan böyük olmalıdır"

        if (marginSum == null || marginSum <= 0) return "Agro məhsul üçün itemMarginSum mütləqdir"
        val expectedMarginSum = "%.2f".format(marginPrice * qty).toDouble()

        if (Math.abs(expectedMarginSum - marginSum) > 0.01) return "Agro: itemMarginSum ($marginSum) = itemMarginPrice×itemQuantity ($expectedMarginSum) olmalıdır"
        if (marginSum > itemSum) return "Agro: itemMarginSum ($marginSum) itemSum ($itemSum)-dən böyük ola bilməz"
        if (creditSum > 0) return "Agro məhsullar kredit ilə satıla bilməz"

        if (prepaymentSum > 0) return "Agro məhsullar avans ilə satıla bilməz"
        return null
    }

    fun validateItemCount(count: Int, creditSum: Double): String? {
        if (count < 1) return "Ən az 1 məhsul olmalıdır"

        if (creditSum > 0 && count > 100) return "Kredit satışında maksimum 100 məhsul ola bilər"

        if (count > 1000) return "Maksimum 1000 məhsul ola bilər"
        return null
    }

    fun validatePaymentSums(
        sum: Double,
        cashSum: Double,
        cashlessSum: Double,
        bonusSum: Double,
        creditSum: Double,
        prepaymentSum: Double,
        incomingSum: Double,
        changeSum: Double,
    ): String? {
        if (sum <= 0) return "Ümumi məbləğ (sum) 0-dan böyük olmalıdır"
        val parts =
            "%.2f".format(cashSum + cashlessSum + bonusSum + creditSum + prepaymentSum).toDouble()


        if (Math.abs(parts - sum) > 0.01) return "sum ($sum) = cashSum + cashlessSum + bonusSum + creditSum + prepaymentSum ($parts) olmalıdır"

        if (cashSum < 0) return "cashSum mənfi ola bilməz"

        if (cashlessSum < 0) return "cashlessSum mənfi ola bilməz"


        if (bonusSum < 0) return "bonusSum mənfi ola bilməz"

        if (creditSum < 0) return "creditSum mənfi ola bilməz"

        if (prepaymentSum < 0) return "prepaymentSum mənfi ola bilməz"


        if (creditSum > 0 && prepaymentSum > 0) return "Kredit (creditSum) və avans (prepaymentSum) eyni anda ola bilməz"
        if (cashSum > 0) {

            if (incomingSum <= 0) return "cashSum > 0 olduqda incomingSum mütləqdir"

            if (incomingSum < cashSum) return "incomingSum ($incomingSum) cashSum ($cashSum) dən kiçik ola bilməz"

            val expectedChange = "%.2f".format(incomingSum - cashSum).toDouble()
            if (Math.abs(expectedChange - changeSum) > 0.01) return "changeSum ($changeSum) = incomingSum - cashSum ($expectedChange) olmalıdır"
        }
        return null
    }

    fun validateVatAmounts(vatAmounts: List<VatAmount>, totalSum: Double): String? {
        if (vatAmounts.isEmpty()) return "vatAmounts boş ola bilməz"

        if (vatAmounts.size > 2) return "vatAmounts maksimum 2 element ola bilər"

        val vatSumTotal = "%.2f".format(vatAmounts.sumOf { it.vatSum }).toDouble()

        if (Math.abs(vatSumTotal - totalSum) > 0.01) return "vatAmounts cəmi ($vatSumTotal) ümumi sum ($totalSum) ilə uyğun gəlmir"

        if (vatAmounts.size == 2) {

            val has18 = vatAmounts.any { it.vatPercent == 18.0 }

            val hasNull = vatAmounts.any { it.vatPercent == null }
            if (!has18 || !hasNull) return "2 elementli vatAmounts yalnız 18% + 0% (vatPercent=null) kombinasiyası ola bilər"
        }
        vatAmounts.forEach { v ->
            if (v.vatPercent != null && v.vatPercent !in listOf(2.0, 8.0, 18.0))
                return "vatPercent yalnız null(0%), 2, 8, 18 ola bilər (cari: ${v.vatPercent})"
        }
        return null
    }


    fun validateRrn(rrn: String?): String? {
        if (rrn.isNullOrBlank()) return null

        if (rrn.length < 12 || rrn.length > 16) return "RRN uzunluğu 12–16 simvol arasında olmalıdır (cari: ${rrn.length})"
        return null
    }

    fun validateParentDocumentId(id: String?): String? {
        if (id.isNullOrBlank()) return null
        if (id.length > 48) return "parentDocument maksimum 48 simvol ola bilər (cari: ${id.length})"
        return null
    }
}

fun validateFullSaleRequest(
    cashier: String,
    currency: String,
    cartItems: List<CartItem>,
    cashSum: Double,
    cashlessSum: Double,
    bonusSum: Double,
    creditSum: Double,
    prepaymentSum: Double,
    incomingSum: Double,
    changeSum: Double,
    totalSum: Double,
    vatAmounts: List<VatAmount>,
    rrn: String? = null,
    parentDocument: String? = null,
): List<String> {
    val errors = mutableListOf<String>()

    SaleValidator.validateCashier(cashier)?.let {
        errors += it
    }

    SaleValidator.validateCurrency(currency)?.let {
        errors += it
    }



    SaleValidator.validatePaymentSums(
        totalSum, cashSum, cashlessSum, bonusSum, creditSum, prepaymentSum, incomingSum, changeSum
    )?.let {
        errors += it
    }


    SaleValidator.validateItemCount(cartItems.size, creditSum)?.let {
        errors += it
    }

    cartItems.forEach { cartItem ->
        val p = cartItem.product
        val qty = cartItem.qty.toFloat()

        SaleValidator.validateItemName(p.name)?.let {
            errors += it
        }



        SaleValidator.validateItemCode(p.code)?.let {
            errors += it
        }

        SaleValidator.validateItemCodeType(
            when (p.barcode.length) {
                8 -> 1; 13 -> 2; else -> 0
            },
            p.barcode.ifBlank { p.code }
        )?.let { errors += it }



        SaleValidator.validateItemQuantity(qty)?.let {
            errors += it
        }


        SaleValidator.validateItemVatPercent(p.vatPercent)?.let { errors += it }

        SaleValidator.validateItemPriceAndSum(cartItem.discountedPrice, qty, cartItem.itemSum)
            ?.let { errors += it }

        if (p.isAgro) {
            SaleValidator.validateAgroItem(
                isAgro = true,
                marginPrice = p.salePrice - p.purchasePrice,
                marginSum = (p.salePrice - p.purchasePrice) * qty,
                itemSum = cartItem.itemSum,
                qty = qty,
                creditSum = creditSum,
                prepaymentSum = prepaymentSum,
            )?.let { errors += it }
        }
    }

    SaleValidator.validateVatAmounts(vatAmounts, totalSum)?.let { errors += it }

    SaleValidator.validateRrn(rrn)?.let { errors += it }
    SaleValidator.validateParentDocumentId(
        parentDocument)?.let { errors += it }

    return errors
}