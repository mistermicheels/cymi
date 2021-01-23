import { Injectable } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { from, Subject } from "rxjs";
import { finalize, tap } from "rxjs/operators";

import { ReloginModalComponent } from "../../shared/relogin-modal/relogin-modal.component";

@Injectable({
    providedIn: "root"
})
export class ReloginService {
    private inProgress = false;

    private reloginResultSubject = new Subject<{ success: boolean }>();
    reloginResult$ = this.reloginResultSubject.asObservable();

    constructor(private modalService: NgbModal) {}

    relogin(email: string) {
        this.inProgress = true;

        const modal = this.modalService.open(ReloginModalComponent);
        modal.componentInstance.email = email;

        return from(modal.result).pipe(
            tap(
                // don't put reloginResult in error state (once Observable is in error state, it stays in error state)
                () => this.reloginResultSubject.next({ success: true }),
                () => this.reloginResultSubject.next({ success: false })
            ),
            finalize(() => (this.inProgress = false))
        );
    }

    isReloginInProgress() {
        return this.inProgress;
    }
}
