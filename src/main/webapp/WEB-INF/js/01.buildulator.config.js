function BuildulatorConfig (config) {
    if (config === undefined) { config = []; }
    this.wysiwygCss = (config.wysiwygCss !== undefined) ? config.wysiwygCss : '/static/css/editor.css';
    this.projectsUrl = (config.projectsUrl !== undefined) ? config.projectsUrl : '/projects/';
    this.projectsNewUrl = (config.projectsNewUrl !== undefined) ? config.projectsNewUrl : '/projects/new';
    this.projectsListAll = (config.projectsListAll !== undefined) ? config.projectsListAll : 'false';
    this.projectsEditUrl = (config.projectsEditUrl !== undefined) ? config.projectsEditUrl : '/projects/?edit';
    this.materialLibraryUrl = (config.materialLibraryUrl !== undefined) ? config.materialLibraryUrl : '/library/materials/';
    this.adminUsersUrl = (config.adminUsersUrl !== undefined) ? config.adminUsersUrl : '/admin/users/';
    this.saveText = (config.saveText !== undefined) ? config.saveText : 'Save';
}