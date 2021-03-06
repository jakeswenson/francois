import {Component, View, FORM_DIRECTIVES, NgFor, NgIf, TemplateRef, CORE_DIRECTIVES} from 'angular2/angular2';
import {ROUTER_DIRECTIVES, RouteParams} from 'angular2/router';
import {TopNav} from './top-nav'
import {FrancoisApi, JobApplication} from './services/francois'
import Collator = Intl.Collator;

@Component({
    templateUrl: '/assets/francois/templates.html',
    directives: [FORM_DIRECTIVES, TopNav, ROUTER_DIRECTIVES, NgFor]
})
export class Templates {

    public templates:any[] = [];

    constructor(private francoisApi:FrancoisApi) {
        var response = francoisApi.getTemplates();

        console.dir(response);

        response.map(r => r.json()).subscribe(
            (value) => value.forEach(template => {
                this.templates.push(template);
                template.parameters = [];
                francoisApi.getTemplateParameters(template.name)
                    .map(r => r.json())
                    .subscribe(params => {
                        params.forEach(p => template.parameters.push(p));
                    });
            }),
            error => console.error(error),
            c => console.log("Templates Loaded"));
    }

}

@Component({
    templateUrl: '/assets/francois/template-jobs.html',
    directives: [FORM_DIRECTIVES, TopNav, ROUTER_DIRECTIVES, NgFor]
})
export class TemplateJobs {

    public jobs:any[] = [];

    public templateName:string;

    constructor(private francoisApi:FrancoisApi, params:RouteParams) {

        this.templateName = params.get('templateName');

        var response = francoisApi.getTemplateJobs(this.templateName);

        console.dir(response);

        response.map(r => r.json())
            .subscribe(
                (value) => value.forEach(job => {
                    this.jobs.push(job);
                }),
                error => console.error(error),
                c => console.log("Jobs Loaded"));
    }

    reapplyTemplate($event:MouseEvent) {

        $event.stopPropagation();
        $event.preventDefault();

        this.francoisApi.reapplyTemplate(this.templateName)
            .subscribe(() => console.log('Template jobs refreshed.'),
                () => console.error('opps, update template jobs failed'));
    }
}

declare module Foundation {
    class Accordian {
    }
}

@Component({
    templateUrl: '/assets/francois/templates-create.html',
    directives: [FORM_DIRECTIVES, TopNav, ROUTER_DIRECTIVES, NgFor, NgIf, CORE_DIRECTIVES],
})
export class CreateJob {

    public parameters:any[] = [];

    public templateName:string;

    public newJobName:string = '';

    public isCreateable:boolean = false;
    public createSucceeded:boolean = false;

    constructor(private francoisApi:FrancoisApi,
                private routeParams:RouteParams) {

        this.templateName = routeParams.get('templateName');

        var response = francoisApi.getTemplateParameters(this.templateName);

        response
            .map(r => r.json())
            .subscribe(params => {
                params.forEach(p => this.parameters.push(p));
                setTimeout(() => {
                    $(document).foundation();
                    this.isCreateable = true;
                }, 30);
            });
    }

    createJob() {
        console.log(this.parameters);
        this.isCreateable = false;
        this.francoisApi.createJob(
            this.templateName,
            new JobApplication(this.newJobName, this.parameters.filter(p => !!p.value)))
            .subscribe(r => {
                console.log(r);
                this.createSucceeded = true;
            }, oops => {
                console.log(oops);
                this.isCreateable = true;
            }, comp => console.log('Create finished'));
    }

    setJobName(evt) {
        console.log(evt.target.value);
        this.newJobName = evt.target.value;
    }

}