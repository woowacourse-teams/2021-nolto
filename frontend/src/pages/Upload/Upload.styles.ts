import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 6rem;

  & > form {
    width: 100%;
  }
`;

const TitleWrapper = styled.h2`
  margin-bottom: 3rem;
`;

export default {
  Root,
  TitleWrapper,
};
