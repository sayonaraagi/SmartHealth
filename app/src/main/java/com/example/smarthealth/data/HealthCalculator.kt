package com.example.smarthealth.data

import com.example.smarthealth.R

data class CalculationResult(
    val bmi: Float,
    val statusResId: Int,
    val recommendationResId: Int = 0,
    val maintenanceCalories: Int = 0,
    val targetCalories: Int = 0,
    val goalResId: Int = 0
)

object HealthCalculator {
    fun calculateBMI(weight: Float, height: Int): Float {
        val hInMeters = height / 100f
        return if (hInMeters > 0) weight / (hInMeters * hInMeters) else 0f
    }

    fun getResult(bmi: Float, weight: Float, height: Int, age: Int, isMale: Boolean): CalculationResult {
        val baseResult = when {
            age < 12 -> CalculationResult(bmi, R.string.status_normal, R.string.rec_child)
            age in 12..17 -> CalculationResult(bmi, getAdolescentStatus(bmi), R.string.rec_adolescent)
            else -> getAdultResult(bmi, isMale)
        }

        // BMR (Mifflin-St Jeor)
        val bmr = if (isMale) {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }
        
        val maintenance = (bmr * 1.2).toInt() // Sedentary
        
        val (target, goalId) = when {
            baseResult.statusResId == R.string.status_underweight -> 
                (maintenance + 400) to R.string.goal_gain
            baseResult.statusResId == R.string.status_normal -> 
                maintenance to R.string.goal_maintain
            else -> 
                (maintenance - 500) to R.string.goal_loss
        }

        return baseResult.copy(
            maintenanceCalories = maintenance,
            targetCalories = target,
            goalResId = goalId
        )
    }

    private fun getAdolescentStatus(bmi: Float): Int {
        return when {
            bmi < 18.5 -> R.string.status_underweight
            bmi < 25 -> R.string.status_normal
            else -> R.string.status_overweight
        }
    }

    private fun getAdultResult(bmi: Float, isMale: Boolean): CalculationResult {
        val statusResId = when {
            isMale -> when {
                bmi < 18.5 -> R.string.status_underweight
                bmi < 25 -> R.string.status_normal
                bmi < 30 -> R.string.status_overweight
                else -> R.string.status_obese
            }
            else -> when {
                bmi < 18.0 -> R.string.status_underweight
                bmi < 24.0 -> R.string.status_normal
                bmi < 29.0 -> R.string.status_overweight
                else -> R.string.status_obese
            }
        }
        
        val recResId = if (bmi >= 30 || (bmi >= 29 && !isMale)) R.string.rec_consult else 0
        
        return CalculationResult(bmi, statusResId, recResId)
    }
}
