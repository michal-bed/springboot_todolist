import {validate, validateOrReject} from "class-validator";

export class BodyValidator {
    static validate(body) {
        validate(body).then(errors => {
            // errors is an array of validation errors
            if (errors.length > 0) {
                console.log('validation failed. errors: ', errors);
            } else {
                console.log('validation succeed');
            }
        });
    }

    static async validateOrReject(body) {
        try {
            await validateOrReject(body);
        } catch (errors) {
            console.log('Caught promise rejection (validation failed). Errors: ', errors);
        }
    }
}