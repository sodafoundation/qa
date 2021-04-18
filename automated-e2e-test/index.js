//require inquirer in the project
const inquirer = require('inquirer')

//require the inquire file tree selection prompt plugin
const inquirerFileTreeSelection = require('inquirer-file-tree-selection-prompt')

// require newman in your project
var newman = require('newman'); 

var optionsObj = {
    collection: '',
    globals: './env_vars/SODA_Globals.postman_globals.json',
    environment: '',
    //exportGlobals: ('./soda_global_vars.json'),
    reporters: ['htmlextra','cli'],
    //bail: true,
    reporter: {
        htmlextra: {
            export: './reports/',
            // template: './template.hbs'
            logs: true,
            // showOnlyFails: true,
            // noSyntaxHighlighting: true,
            // testPaging: true,
            browserTitle: "",
            title: "",
            titleSize: 4,
            // omitHeaders: true,
            skipHeaders: "X-Auth-Token",
            hideRequestBody: ["Register Backend", "Register Backend Invalid Credentials"],
            hideResponseBody: ["Register Backend", "Register Backend Invalid Credentials"],
            // showEnvironmentData: true,
            // skipEnvironmentVars: ["API_KEY"],
            // showGlobalData: true,
            skipGlobalVars: ["authToken"],
            // skipSensitiveData: true,
            // showMarkdownLinks: true,
            // showFolderDescription: true,
            // timezone: "Australia/Sydney"
        }
    },
    insecure: true, // allow self-signed certs, required in postman too,
    delayRequest: 2000,
    //timeout: 180000  // set time out,
};

var collectionSource = '';
//register the file tree selection prompt plugin with inquirer
inquirer.registerPrompt('file-tree-selection', inquirerFileTreeSelection)
console.log("****************************************")
console.log("****************************************")
console.log("Welcome to the SODA E2E API Testing App!");
console.log("****************************************")
console.log("****************************************")
var questions = [
    {
        type: 'list',
        name: 'module',
        message: 'Select the module for which you want to run E2E tests?',
        choices: [
            {
                name: 'SODA API',
                value: 'soda-api'
            },
            {
                name: 'SODA Multi-cloud',
                value: 'soda-multicloud'
            },
            new inquirer.Separator(),
            {
                name: 'SODA Multi-cloud Migration',
                disabled: 'Unavailable at this time',
            },
            {
                name: 'SODA Multi-cloud Block',
                disabled: 'Unavailable at this time',
            },
            {
                name: 'SODA Multi-cloud File',
                disabled: 'Unavailable at this time',
            },
            {
                name: 'SODA Delfin',
                disabled: 'Unavailable at this time',
            }          
        ],
      }
  ];

inquirer
  .prompt(questions)
  .then(answers => {
      console.log("Selected module", answers['module']);
    switch(answers['module']){
        case 'soda-api' : 
            collectionSource = 'SODA API';
            optionsObj.collection = './collections/SODA_API_Automation.postman_collection.json';
            optionsObj.environment = './env_vars/SODA_API.postman_environment.json';
            optionsObj.reporter.htmlextra.browserTitle = 'SODA API E2E Test report';
            optionsObj.reporter.htmlextra.title = 'SODA API E2E Test report';
            break;
        case 'soda-multicloud' : 
            collectionSource = 'SODA Multicloud';
            optionsObj.collection = './collections/SODA_Multicloud_Automation_Test.postman_collection.json';
            optionsObj.environment = './env_vars/SODA_MULTICLOUD.postman_environment.json';
            optionsObj.reporter.htmlextra.browserTitle = 'SODA Multicloud E2E Test report';
            optionsObj.reporter.htmlextra.title = 'SODA Multicloud E2E Test report';
            optionsObj['iterationData'] = './iteration_data/multicloud_iteration_data.json';
            break;
        default: 
            console.log("No Collection selected.");
            break;
    }
    
    // call newman.run to pass `options` object and wait for callback
    newman.run(optionsObj)
    .on('start', function (err, args) { // on start of run, log to console
        console.log('Running E2E tests for:', collectionSource);
    }).on('done', function (err, summary) {
        if (err || summary.error) {
            console.error('collection run encountered an error.', err);
            console.log("Summary Error",summary.error);
        }
        else {
            console.log('collection run completed.');
            console.log("The test report is generated under /reports.")
        }
    });
    
  });