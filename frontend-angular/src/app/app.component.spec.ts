import { async, TestBed } from "@angular/core/testing";
import { RouterTestingModule } from "@angular/router/testing";

import { AppComponent } from "./app.component";

describe("AppComponent", () => {
    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [RouterTestingModule],
            declarations: [AppComponent]
        }).compileComponents();
    }));

    it("should pass a dummy test", () => {
        expect(true).toBeTruthy();
    });
});