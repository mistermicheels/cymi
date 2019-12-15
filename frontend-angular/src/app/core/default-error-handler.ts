import { HttpErrorResponse } from "@angular/common/http";
import { ErrorHandler } from "@angular/core";

export class DefaultErrorHandler implements ErrorHandler {
    handleError(error: any): void {
        if (!(error instanceof HttpErrorResponse)) {
            console.error(error);
        }
    }
}
