package ru.a_party.annuitycalc

import java.util.*

class AnnuityCalc {

    companion object {

        private var listPay: List<Pair<Date, Double>> = listOf()

        fun getOversizePaid(month: Int, monthPay: Double, creditSumm: Double) =
            month * monthPay - creditSumm

        private fun annuityK(internalRate: Double, internalTimeMonth: Int): Double {
            return if (internalTimeMonth <= 0) {
                0.00
            } else internalRate * Math.pow(
                1f + internalRate,
                internalTimeMonth.toDouble()
            ) / (Math.pow(
                1f + internalRate,
                internalTimeMonth.toDouble()
            ) - 1f)
        }

        fun monthPay(rate: Double, timeMonth: Int, creditSumm: Double): Double =
            creditSumm * annuityK(rate, timeMonth)

        fun calcRateBySummAndPaySumm(
            creditSumm: Double,
            timeMonth: Int,
            monthPay: Double
        ): Double? {

            var pay = 0.0
            var found = false
            var i: Double
            var findRate:Double?=null

            i = 0.0
            while (i < 100000) {
                val annuityK = annuityK(i / 100 / 100 / 12, timeMonth)
                pay = creditSumm * annuityK
                if (pay >= monthPay) {
                    findRate = i / 100
                    break
                }
                i++
            }
            return findRate
        }
    }

}