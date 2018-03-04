<signup>
  <h2 class="content-title">Sign Up</h2>
  <section class="sign-in-box">
    <span class="errors" if={validationErrors._global}> {validationErrors._global}</span>
    <form if={!mailSent} class="signup-form">
      <span la:errors="_global"></span>
      <ul>
        <li>
          <span>Name</span>
          <input type="text" ref="memberName" />
          <span if={validationErrors.memberName} class="errors"> {validationErrors.memberName}</span>
        </li>
        <li>
          <span>Account</span>
          <input type="text" ref="memberAccount" />
          <span if={validationErrors.memberAccount} class="errors"> {validationErrors.memberAccount}</span>
        </li>
        <li>
          <span>Password</span>
          <input type="password" ref="password" />
          <span if={validationErrors.password} class="errors"> {validationErrors.password}</span>
        </li>
        <li>
          <span>Reminder Question</span>
          <input type="text" ref="reminderQuestion" />
          <span if={validationErrors.reminderQuestion} class="errors"> {validationErrors.reminderQuestion}</span>
        </li>
        <li>
          <span>Reminder Answer</span>
          <input type="text" ref="reminderAnswer" />
          <span if={validationErrors.reminderAnswer} class="errors"> {validationErrors.reminderAnswer}</span>
        </li>
        <li>
          <button class="btn btn-success" onclick={onRegister}>Sign Up</button>
        </li>
      </ul>
    </form>
    <div if={mailSent}>
      <p>Email was sent to you with an activation link. </p>
      <p>Please click the link in that email to activate your account and to verify your email address.</p>
    </div>
  </section>

  <style>
    .sign-in-box {
      width: 420px;
      margin: 32px auto;
      border: solid 4px #E5E5E5;
      padding: 24px 64px;
    }

    .signup-form input[type="text"],
    .signup-form input[type="password"] {
      margin-top: 4px;
      padding: 6px 8px;
      width: 80%;
    }

    .signup-form input[type="submit"] {
      margin-top: 8px;
      padding: 10px 32px;
      height: 36px;
      background-color: #882255;
      border: 1px solid #882255;
      color: #FFF;
      line-height: 1;
      font-size: 14px;
      font-weight: bold;
      vertical-align: top;
      cursor: pointer;
    }
  </style>

  <script>
    var self = this;
    this.mixin('common')

    this.validationErrors = {};
    this.mailSent = false;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.onRegister = (e) => {
      e.preventDefault();
      request.post(this.api.auth.signup, self.getQueryParams(),
        () => {
          self.mailSent = true;
          self.update();
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        },
        true);
    };

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    this.getQueryParams = () => {
      return {
        memberName: self.refs.memberName.value,
        memberAccount: self.refs.memberAccount.value,
        password: self.refs.password.value,
        reminderQuestion: self.refs.reminderQuestion.value,
        reminderAnswer: self.refs.reminderAnswer.value
      };
    };
  </script>
</signup>