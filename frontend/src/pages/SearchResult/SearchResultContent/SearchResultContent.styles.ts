import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const RecentFeedsContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-gap: 1rem;
`;

const MoreHiddenElement = styled.div`
  width: 100%;
  height: 20px;
  visibility: hidden;
`;

export default {
  Root,
  RecentFeedsContainer,
  MoreHiddenElement,
};
