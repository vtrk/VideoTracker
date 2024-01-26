import { Injectable } from "@angular/core";
import { AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class PasswordValidationService {
  validatePassword(password: string): string[] {
    const errors: string[] = [];

    const hasUpperCase = /[A-Z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecialCharacter = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password);

    if(!hasUpperCase){
      errors.push("Password must contain at least one uppercase letter");
    }
    if(!hasNumber){
      errors.push("Password must contain at least one number");
    }
    if(!hasSpecialCharacter){
      errors.push("Password must contain at least one special character");
    }
    return errors;
  }
}
