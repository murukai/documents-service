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

describe('MarriageDetails e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load MarriageDetails', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('MarriageDetails').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details MarriageDetails page', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('marriageDetails');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create MarriageDetails page', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MarriageDetails');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit MarriageDetails page', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('MarriageDetails');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of MarriageDetails', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('MarriageDetails');

    cy.get(`[data-cy="dateOfMarriage"]`).type('2021-04-03').should('have.value', '2021-04-03');


    cy.get(`[data-cy="spouseFullName"]`).type('Bulgarian invoice', { force: true }).invoke('val').should('match', new RegExp('Bulgarian invoice'));


    cy.get(`[data-cy="placeOfMarriage"]`).type('Granite bypass', { force: true }).invoke('val').should('match', new RegExp('Granite bypass'));


    cy.get(`[data-cy="spousePlaceOfBirth"]`).type('Dynamic Gorgeous monitor', { force: true }).invoke('val').should('match', new RegExp('Dynamic Gorgeous monitor'));


    cy.get(`[data-cy="countryOfMarriage"]`).type('Somoni Berkshire invoice', { force: true }).invoke('val').should('match', new RegExp('Somoni Berkshire invoice'));


    cy.get(`[data-cy="spouseCountryOfBirth"]`).type('navigating Lead', { force: true }).invoke('val').should('match', new RegExp('navigating Lead'));


    cy.get(`[data-cy="marriageNumber"]`).type('redundant grey', { force: true }).invoke('val').should('match', new RegExp('redundant grey'));


    cy.get(`[data-cy="marriedBefore"]`).should('not.be.checked');
    cy.get(`[data-cy="marriedBefore"]`).click().should('be.checked');

    cy.get(`[data-cy="marriageOrder"]`).type('Lane Avon', { force: true }).invoke('val').should('match', new RegExp('Lane Avon'));


    cy.get(`[data-cy="devorceOrder"]`).type('up Account Maine', { force: true }).invoke('val').should('match', new RegExp('up Account Maine'));


    cy.get(`[data-cy="previousSppouses"]`).type('architectures Producer Borders', { force: true }).invoke('val').should('match', new RegExp('architectures Producer Borders'));

    cy.setFieldSelectToLastOfEntity('applicant');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of MarriageDetails', () => {
    cy.intercept('GET', '/api/marriage-details*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/marriage-details/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('marriage-details');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('marriageDetails').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/marriage-details*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('marriage-details');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
