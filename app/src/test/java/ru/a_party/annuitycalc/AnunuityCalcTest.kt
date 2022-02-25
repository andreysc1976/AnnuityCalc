package ru.a_party.annuitycalc

import org.junit.Test

import org.junit.Assert.*


class AnunuityCalcTest {
    @Test
    fun checkOversizePaidV1(){
        assertEquals(AnnuityCalc.getOversizePaid(0,1.00,12.00),-12.00,0.1)
    }

    @Test
    fun checkOversizePaidV2(){
        assertEquals(AnnuityCalc.getOversizePaid(2,12.00,20.00),4.00,0.01)
    }

    @Test
    fun checkOversizePaidV3(){
        assertEquals(AnnuityCalc.getOversizePaid(12,12.00,144.00),0.00,0.1)
    }

    @Test
    fun checkMonthPayV1(){
        assertEquals(AnnuityCalc.monthPay(11.00/100/12,12,100000.00),8838.17,0.1)
    }

    @Test
    fun checkMonthPayV2(){
        assertEquals(AnnuityCalc.monthPay(1.00/100/12,12,100000.00),8378.50,0.1)
    }

    @Test
    fun checkMonthPayV3(){
        assertEquals(AnnuityCalc.monthPay(11.00/100/12,12,-100000.00),-8838.17,0.1)
    }

    @Test
    fun checkBigSumm() {
        assertNull(AnnuityCalc.calcRateBySummAndPaySumm(12000.00,5,100000.00))
    }

    fun checkNormalSumm() {
        assertNotNull(AnnuityCalc.calcRateBySummAndPaySumm(12000.00,5,1000.00))
    }
}