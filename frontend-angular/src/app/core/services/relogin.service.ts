import { Injectable } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { from, Subject } from "rxjs";
import { tap } from "rxjs/operators";

import { ReloginModalComponent } from "../../shared/relogin-modal/relogin-modal.component";

@Injectable({
    providedIn: "root"
})
export class ReloginService {
    private inProgress = false;

    private completedSubject = new Subject<void>();
    completed$ = this.completedSubject.asObservable();

    constructor(private modalService: NgbModal) {}

    relogin(email: string) {
        this.inProgress = true;

        const modal = this.modalService.open(ReloginModalComponent);
        modal.componentInstance.email = email;

        return from(modal.result).pipe(
            tap(
                () => this.completedSubject.next(),
                error => this.completedSubject.error(error)
            )
        );
    }

    isReloginInProgress() {
        return this.inProgress;
    }
}
