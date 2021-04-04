import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Applicant e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Applicants', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Applicant').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Applicant page', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('applicant');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Applicant page', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Applicant');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Applicant page', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Applicant');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Applicant', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Applicant');

    cy.get(`[data-cy="surname"]`).type('mindshare visionary Brazilian', { force: true }).invoke('val').should('match', new RegExp('mindshare visionary Brazilian'));


    cy.get(`[data-cy="otherNames"]`).type('invoice Bacon', { force: true }).invoke('val').should('match', new RegExp('invoice Bacon'));


    cy.get(`[data-cy="maidenName"]`).type('JBOD eyeballs', { force: true }).invoke('val').should('match', new RegExp('JBOD eyeballs'));


    cy.get(`[data-cy="changedName"]`).should('not.be.checked');
    cy.get(`[data-cy="changedName"]`).click().should('be.checked');

    cy.get(`[data-cy="gender"]`).select('MALE');


    cy.get(`[data-cy="maritalStatus"]`).select('SINGLE');


    cy.get(`[data-cy="dateOfBirth"]`).type('2021-04-04').should('have.value', '2021-04-04');


    cy.get(`[data-cy="idNumber"]`).type('Credit eyeballs', { force: true }).invoke('val').should('match', new RegExp('Credit eyeballs'));


    cy.get(`[data-cy="birthEntryNumber"]`).type('Concrete', { force: true }).invoke('val').should('match', new RegExp('Concrete'));

    cy.setFieldSelectToLastOfEntity('democraphicDetails');

    cy.setFieldSelectToLastOfEntity('declaration');

    cy.setFieldSelectToLastOfEntity('guardian');

    cy.setFieldSelectToLastOfEntity('user');

    cy.setFieldSelectToLastOfEntity('appointmentSlot');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/applicants*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Applicant', () => {
    cy.intercept('GET', '/api/applicants*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/applicants/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('applicant');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('applicant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/applicants*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('applicant');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
