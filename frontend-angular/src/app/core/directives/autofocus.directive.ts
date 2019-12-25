import { Directive, ElementRef, OnInit } from "@angular/core";

@Directive({ selector: "[appAutofocus]" })
export class AutofocusDirective implements OnInit {
    private host?: HTMLElement;

    constructor(elementRef: ElementRef) {
        this.host = elementRef.nativeElement;
    }

    ngOnInit(): void {
        if (this.host) {
            setTimeout(() => {
                this.host!.focus();

                if (this.host instanceof HTMLInputElement) {
                    this.host.select();
                }
            });
        }
    }
}
