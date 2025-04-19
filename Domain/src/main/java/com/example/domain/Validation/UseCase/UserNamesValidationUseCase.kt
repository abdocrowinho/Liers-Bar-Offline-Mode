package com.example.domain.Validation

 class UserNamesValidationUseCase(){
operator fun invoke(userName1:String,userName2:String
,userName3:String,userName4:String):ValidationResult{
    val fields = listOf(userName1,userName2,userName3,userName4)

    fields.forEachIndexed { index, userName ->

        if (userName.isBlank()){
            return ValidationResult(false, errorMessage = "field is required"
                , errorField = index.plus(1))
        }else{
            if (userName.length<4){
                return ValidationResult(false,
                    errorMessage = "field cant be less than 4 letters"
                , errorField = index.plus(1)
                )
            }
        }
    }
return ValidationResult(true)
}
}

data class ValidationResult(
    val isValid:Boolean ,
    val errorMessage : String?=null,
    val errorField : Int?=null
)